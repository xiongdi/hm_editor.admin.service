package com.hm.editor.adminservice.console.repository;

import com.hm.editor.adminservice.console.domain.EmrBaseFolder;
import com.hm.editor.adminservice.console.domain.EmrBaseTemplate;
import com.hm.editor.adminservice.console.domain.EmrDataElementAdmin;
import com.hm.editor.adminservice.console.domain.EmrDataSet;
import java.util.*;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class BaseFolderRepository {
  private String FOLDER_NAME = "emrBaseFolder";
  private String BASE_NAME = "emrBaseTemplate";
  private String DATASET_NAME = "emrDataSet";

  @Autowired MongoOperations template;

  public List<EmrDataSet> getAll() {
    ObjectId o = new ObjectId();
    o.toHexString();
    return template.findAll(EmrDataSet.class, FOLDER_NAME);
  }

  public List<EmrDataElementAdmin> getCondition(String id) {
    Query query = new Query();
    query.addCriteria(Criteria.where("folderId").is(new ObjectId(id)));
    return template.find(query, EmrDataElementAdmin.class, BASE_NAME);
  }

  public boolean delBaseFolder(String id) {
    return template
            .remove(Query.query(Criteria.where("_id").is(new ObjectId(id))), DATASET_NAME)
            .getDeletedCount()
        > 0;
  }

  public boolean editBaseFolder(Map<String, Object> emrBaseFolder) {
    HashMap<String, Object> map = new HashMap<>();
    Object elementSet = emrBaseFolder.get("elementSet");
    map.put("description", emrBaseFolder.get("description"));
    map.put("template", new ObjectId(String.valueOf(emrBaseFolder.get("template"))));
    map.put("createDate", new Date());
    map.put("__v", "0");
    map.put("elementSet", new ObjectId(String.valueOf(elementSet)));
    template.insert(map, DATASET_NAME);
    return true;
  }

  public Map getDataSet(Map<String, Object> param) {

    Map _r = new HashMap();

    Map<String, Integer> page = (Map<String, Integer>) param.get("page");
    int currentPage = page.get("currentPage");
    int pageSize = page.get("pageSize");

    String name = param.get("templateTrueName").toString();

    Map<String, Object> td =
        template.findOne(
            Query.query(Criteria.where("templateTrueName").is(name)),
            Map.class,
            "emrTemplateDatasource");

    List<Map<String, Object>> res = new ArrayList<>();
    if (td != null && td.containsKey("dataSourceSet")) {
      int size;
      Object o = td.get("dataSourceSet");
      if (o instanceof List && (size = ((List) o).size()) > 0) {
        List subo = (List) o;

        final Object key = param.get("key");

        if (key != null && key.toString().trim().length() > 0) {
          o =
              ((List) o)
                  .stream()
                      .filter(
                          s -> {
                            Map _s = (Map) s;
                            return _s.containsKey("name")
                                && _s.get("name") != null
                                && _s.get("name").toString().contains(key.toString());
                          })
                      .collect(Collectors.toList());
          if ((size = ((List) o).size()) == 0) {
            return _r;
          }
        }
        if (size > (currentPage - 1) * pageSize) {
          subo =
              ((List) o)
                  .subList(
                      (currentPage - 1) * pageSize,
                      size > currentPage * pageSize ? currentPage * pageSize : size);
        }

        subo.forEach(
            oi -> {
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

  public EmrBaseFolder getFolderByName(String name) {
    return template.findOne(Query.query(Criteria.where("name").is(name)), EmrBaseFolder.class);
  }

  public EmrBaseTemplate getTemplateByName(String name) {
    return template.findOne(
        Query.query(Criteria.where("templateName").is(name)), EmrBaseTemplate.class);
  }

  public int nextFolderIndex() {
    return Integer.valueOf(template.count(new Query(), EmrBaseFolder.class) + "") + 1;
  }

  public void save(Object d) {
    template.save(d);
  }
}
