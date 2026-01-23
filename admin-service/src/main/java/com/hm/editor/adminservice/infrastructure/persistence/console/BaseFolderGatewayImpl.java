package com.hm.editor.adminservice.infrastructure.persistence.console;

import com.hm.editor.adminservice.domain.console.gateway.BaseFolderGateway;
import com.hm.editor.adminservice.domain.console.model.EmrDataElementAdmin;
import com.hm.editor.adminservice.domain.console.model.EmrDataSet;
import com.hm.editor.adminservice.domain.console.model.EmrBaseFolder;
import com.hm.editor.adminservice.domain.console.model.EmrBaseTemplate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * 基础目录Gateway实现
 */
@Component
public class BaseFolderGatewayImpl implements BaseFolderGateway {

    private static final String FOLDER_NAME = "emrBaseFolder";
    private static final String BASE_NAME = "emrBaseTemplate";
    private static final String DATASET_NAME = "emrDataSet";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<EmrDataSet> getAll() {
        return mongoTemplate.findAll(EmrDataSet.class, FOLDER_NAME);
    }

    @Override
    public List<EmrDataElementAdmin> getCondition(String id) {
        if (id == null || !ObjectId.isValid(id)) {
            return new java.util.ArrayList<>();
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("folderId").is(new ObjectId(id)));
        return mongoTemplate.find(query, EmrDataElementAdmin.class, BASE_NAME);
    }

    @Override
    public Map<String, Object> getDataSet(Map<String, Object> param) {
        Map<String, Object> _r = new HashMap<>();

        Map<String, Integer> page = (Map<String, Integer>) param.get("page");
        int currentPage = page.get("currentPage");
        int pageSize = page.get("pageSize");

        String name = param.get("templateTrueName").toString();

        Map<String, Object> td = mongoTemplate.findOne(
            Query.query(Criteria.where("templateTrueName").is(name)),
            Map.class,
            "emrTemplateDatasource"
        );

        List<Map<String, Object>> res = new java.util.ArrayList<>();
        if (td != null && td.containsKey("dataSourceSet")) {
            int size;
            Object o = td.get("dataSourceSet");
            if (o instanceof List && (size = ((List<?>) o).size()) > 0) {
                @SuppressWarnings("unchecked")
                List<Object> subo = (List<Object>) o;

                final Object key = param.get("key");

                if (key != null && !key.toString().trim().isEmpty()) {
                    @SuppressWarnings("unchecked")
                    List<Object> filteredList = ((List<Object>) o).stream()
                        .filter(s -> {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> _s = (Map<String, Object>) s;
                            return (
                                _s.containsKey("name") &&
                                _s.get("name") != null &&
                                _s.get("name").toString().contains(key.toString())
                            );
                        })
                        .collect(Collectors.toList());
                    o = filteredList;
                    if ((size = filteredList.size()) == 0) {
                        return _r;
                    }
                }
                if (size > (currentPage - 1) * pageSize) {
                    @SuppressWarnings("unchecked")
                    List<Object> sourceList = (List<Object>) o;
                    subo = sourceList.subList(
                        (currentPage - 1) * pageSize,
                        Math.min(currentPage * pageSize, size)
                    );
                }

                subo.forEach(oi -> {
                    Map<String, Object> r = new HashMap<>();
                    r.put("elementSet", oi);
                    res.add(r);
                });

                _r.put("data", res);
                _r.put("total", size);
            }
        }
        return _r;
    }

    @Override
    public boolean deleteBaseFolder(String id) {
        return (
            mongoTemplate
                .remove(Query.query(Criteria.where("_id").is(new ObjectId(id))), DATASET_NAME)
                .getDeletedCount() >
            0
        );
    }

    @Override
    public boolean addBaseFolder(Map<String, Object> emrBaseFolder) {
        try {
            HashMap<String, Object> map = new HashMap<>();
            Object elementSet = emrBaseFolder.get("elementSet");
            map.put("description", emrBaseFolder.get("description"));
            if (emrBaseFolder.get("template") != null) {
                map.put("template", new ObjectId(String.valueOf(emrBaseFolder.get("template"))));
            }
            map.put("createDate", new Date());
            map.put("editDate", new Date());
            map.put("__v", 0);
            if (elementSet != null) {
                map.put("elementSet", new ObjectId(String.valueOf(elementSet)));
            }
            if (emrBaseFolder.get("folderName") != null) {
                map.put("folderName", emrBaseFolder.get("folderName"));
            }
            if (emrBaseFolder.get("dataSourceName") != null) {
                map.put("dataSourceName", emrBaseFolder.get("dataSourceName"));
            }
            if (emrBaseFolder.get("dataSourceType") != null) {
                map.put("dataSourceType", emrBaseFolder.get("dataSourceType"));
            }
            mongoTemplate.insert(map, DATASET_NAME);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean editBaseFolder(Map<String, Object> emrBaseFolder) {
        try {
            Object id = emrBaseFolder.get("_id");
            if (id == null) {
                return addBaseFolder(emrBaseFolder);
            }

            Query query = Query.query(Criteria.where("_id").is(new ObjectId(String.valueOf(id))));
            Update update = new Update();

            if (emrBaseFolder.get("description") != null) {
                update.set("description", emrBaseFolder.get("description"));
            }
            if (emrBaseFolder.get("template") != null) {
                update.set("template", new ObjectId(String.valueOf(emrBaseFolder.get("template"))));
            }
            if (emrBaseFolder.get("elementSet") != null) {
                update.set(
                    "elementSet",
                    new ObjectId(String.valueOf(emrBaseFolder.get("elementSet")))
                );
            }
            if (emrBaseFolder.get("folderName") != null) {
                update.set("folderName", emrBaseFolder.get("folderName"));
            }
            if (emrBaseFolder.get("dataSourceName") != null) {
                update.set("dataSourceName", emrBaseFolder.get("dataSourceName"));
            }
            if (emrBaseFolder.get("dataSourceType") != null) {
                update.set("dataSourceType", emrBaseFolder.get("dataSourceType"));
            }

            update.set("editDate", new Date());
            update.set("__v", "0");

            mongoTemplate.updateFirst(query, update, DATASET_NAME);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public EmrBaseFolder getFolderByName(String name) {
        return mongoTemplate.findOne(
            Query.query(Criteria.where("name").is(name)),
            EmrBaseFolder.class
        );
    }

    @Override
    public EmrBaseTemplate getTemplateByName(String name) {
        return mongoTemplate.findOne(
            Query.query(Criteria.where("templateName").is(name)),
            EmrBaseTemplate.class
        );
    }

    @Override
    public int nextFolderIndex() {
        return Integer.parseInt(String.valueOf(mongoTemplate.count(new Query(), EmrBaseFolder.class))) +
            1;
    }

    @Override
    public void save(Object entity) {
        mongoTemplate.save(entity);
    }
}
