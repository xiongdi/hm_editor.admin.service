package com.hm.editor.adminservice.infrastructure.persistence.console;

import static org.springframework.data.mongodb.core.query.Criteria.*;

import com.hm.editor.adminservice.domain.console.gateway.DataElementGateway;
import com.hm.editor.adminservice.domain.console.model.EmrDataElementAdmin;
import com.hm.editor.adminservice.infrastructure.persistence.console.DataElementRepository;
import com.hm.editor.adminservice.infrastructure.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * 数据元Gateway实现
 */
@Component
public class DataElementGatewayImpl implements DataElementGateway {

    private static final String COLLECTION_NAME = "emrDataElementAdmin";

    @Autowired
    private DataElementRepository dataElementRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<EmrDataElementAdmin> findAll(Map<String, Object> param) {
        Map<String, Integer> page = (Map<String, Integer>) param.get("page");
        if (page == null) {
            return new java.util.ArrayList<>();
        }
        String key = String.valueOf(param.get("key"));
        int currentPage = page.get("currentPage");
        int pageSize = page.get("pageSize");

        Query query = new Query();
        Criteria cri = new Criteria();
        if (StringUtils.isNotBlank(key)) {
            cri.and("dataSourceName").regex(key, "i");
        }
        Object tname = param.get("templateTrueName");
        if (tname != null && !tname.toString().isEmpty()) {
            cri.and("templateName").regex(tname + "");
        }
        query.addCriteria(cri);
        query
            .skip((long) (currentPage - 1) * pageSize)
            .limit(pageSize)
            .with(Sort.by(Sort.Direction.DESC, "createDate"));
        return mongoTemplate.find(query, EmrDataElementAdmin.class, COLLECTION_NAME);
    }

    @Override
    public long count(Map<String, Object> param) {
        String key = String.valueOf(param.get("key"));
        Object tname = param.get("templateTrueName");
        Criteria cri = new Criteria();
        if (StringUtils.isNotBlank(key)) {
            cri.and("dataSourceName").regex(key, "i");
        }
        if (tname != null && !tname.toString().isEmpty()) {
            cri.and("templateName").regex(tname.toString());
        }
        return mongoTemplate.count(Query.query(cri), COLLECTION_NAME);
    }

    @Override
    public boolean deleteById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, COLLECTION_NAME);
        return true;
    }

    @Override
    public boolean update(Map<String, Object> param) {
        String id = String.valueOf(param.get("_id"));
        Query query = Query.query(where("_id").is(new ObjectId(id)));
        Update update = new Update();
        update.set("dataSourceName", param.get("dataSourceName"));
        update.set("typeCode", param.get("typeCode"));
        update.set("typeItems", param.get("typeItems"));
        update.set("placeholder", param.get("placeholder"));
        update.set("searchOption", param.get("searchOption"));
        update.set("searchPair", param.get("searchPair"));
        update.set("printReplacement", param.get("printReplacement"));
        update.set("printMinWidth", param.get("printMinWidth"));
        update.set("printUnderline", param.get("printUnderline"));
        update.set("printColor", param.get("printColor"));
        update.set("printBorder", param.get("printBorder"));
        update.set("enterAutoGrow", param.get("enterAutoGrow"));
        update.set("autoshowcurtime", param.get("autoshowcurtime"));
        update.set("isdisabled", param.get("isdisabled"));
        update.set("description", param.get("description"));
        update.set("editDate", param.get("editDate"));
        update.set("createDate", param.get("createDate"));
        update.set("__v", param.get("__v"));
        update.set("templateName", param.get("templateName"));

        mongoTemplate.upsert(query, update, COLLECTION_NAME);
        return true;
    }

    @Override
    public boolean insert(Map<String, Object> param) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("dataSourceName", param.get("dataSourceName"));
        map.put("typeCode", param.get("typeCode"));
        map.put("typeItems", param.get("typeItems"));
        map.put("placeholder", param.get("placeholder"));
        map.put("searchOption", param.get("searchOption"));
        map.put("searchPair", param.get("searchPair"));
        map.put("printReplacement", param.get("printReplacement"));
        map.put("printMinWidth", param.get("printMinWidth"));
        map.put("printUnderline", param.get("printUnderline"));
        map.put("printColor", param.get("printColor"));
        map.put("printBorder", param.get("printBorder"));
        map.put("enterAutoGrow", param.get("enterAutoGrow"));
        map.put("autoshowcurtime", param.get("autoshowcurtime"));
        map.put("isdisabled", param.get("isdisabled"));
        map.put("description", param.get("description"));
        map.put("editDate", param.get("editDate"));
        map.put("createDate", param.get("createDate"));
        map.put("templateName", param.get("templateName"));
        map.put("key", param.get("key"));
        map.put("searchOptionObj", param.get("searchOptionObj"));
        map.put("printUnderlineObj", param.get("printUnderlineObj"));
        map.put("printColorObj", param.get("printColorObj"));
        map.put("printBorderObj", param.get("printBorderObj"));
        map.put("enterAutoGrowObj", param.get("enterAutoGrowObj"));
        map.put("autoshowcurtimeObj", param.get("autoshowcurtimeObj"));
        map.put("isdisabledObj", param.get("isdisabledObj"));
        map.put("dataSourceType", param.get("dataSourceType"));
        map.put("typeName", param.get("typeName"));
        map.put("_v", param.get("_v"));
        try {
            mongoTemplate.insert(map, COLLECTION_NAME);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<Map<String, Object>> searchDataElement(String dataSourceName, String templateName) {
        Query query = new Query();
        query.addCriteria(
            Criteria.where("dataSourceName").is(dataSourceName).and("templateName").is(templateName)
        );
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> res = (List<Map<String, Object>>) (List<?>) mongoTemplate.find(
            query,
            Map.class,
            COLLECTION_NAME
        );
        return res;
    }

    @Override
    public List<EmrDataElementAdmin> getAllData() {
        Query query = new Query();
        return mongoTemplate.find(query, EmrDataElementAdmin.class, COLLECTION_NAME);
    }

    @Override
    public void batchInsert(List<EmrDataElementAdmin> data) {
        mongoTemplate.insert(data, EmrDataElementAdmin.class);
    }

    @Override
    public void autoBind(Map<String, Object> data) {
        Update u = new Update();
        if (data != null && data.containsKey("dataSourceSet")) {
            u.set("dataSourceSet", data.get("dataSourceSet"));
        }
        mongoTemplate.upsert(
            Query.query(Criteria.where("templateTrueName").is(data.get("templateTrueName"))),
            u,
            "emrTemplateDatasource"
        );
    }

    @Override
    public List<EmrDataElementAdmin> findByName(Set<String> name, String templateName) {
        return mongoTemplate.find(
            Query.query(
                Criteria.where("dataSourceName").in(name).and("templateName").is(templateName)
            ),
            EmrDataElementAdmin.class
        );
    }
}
