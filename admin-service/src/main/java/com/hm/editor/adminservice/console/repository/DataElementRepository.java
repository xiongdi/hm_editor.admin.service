package com.hm.editor.adminservice.console.repository;

import static org.springframework.data.mongodb.core.query.Criteria.*;

import com.hm.editor.adminservice.console.domain.EmrDataElementAdmin;
import com.hm.editor.common.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class DataElementRepository {

    private static final String FOLDER_NAME = "emrDataElementAdmin";

    @Autowired
    MongoTemplate template;

    public List<EmrDataElementAdmin> getAll(Map<String, Object> param) {
        Map<String, Integer> page = (Map<String, Integer>) param.get("page");
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
            .skip((currentPage - 1) * pageSize)
            .limit(pageSize)
            .with(Sort.by(Sort.Direction.DESC, "createDate"));
        return template.find(query, EmrDataElementAdmin.class, FOLDER_NAME);
    }

    public boolean delDataElement(String param) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(param));
        template.remove(query, FOLDER_NAME);
        return true;
    }

    public boolean updDataElement(Map<String, Object> param) {
        String id = String.valueOf(param.get("_id"));
        ObjectId o = new ObjectId(id);
        o.toHexString();
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

        template.upsert(query, update, FOLDER_NAME);
        return true;
    }

    public List<Map<String, Object>> searchDataElement(String dataSourceName, String templateName) {
        List<Map<String, Object>> list;
        Query query = new Query();
        query.addCriteria(
            Criteria.where("dataSourceName").is(dataSourceName).and("templateName").is(templateName)
        );
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> res = (List<Map<String, Object>>) (List<?>) template.find(
            query,
            Map.class,
            FOLDER_NAME
        );
        list = res;
        return list;
    }

    public boolean intDataElement(Map<String, Object> param) {
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
            template.insert(map, FOLDER_NAME);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public long getTotal(Map<String, Object> param) {
        String key = String.valueOf(param.get("key"));
        Object tname = param.get("templateTrueName");
        Criteria cri = new Criteria();
        if (StringUtils.isNotBlank(key)) {
            cri.and("dataSourceName").regex(key, "i");
        }
        if (tname != null && !tname.toString().isEmpty()) {
            cri.and("templateName").regex(tname.toString());
        }
        return template.count(Query.query(cri), FOLDER_NAME);
    }

    public List<EmrDataElementAdmin> getAllData() {
        Query query = new Query();

        return template.find(query, EmrDataElementAdmin.class, FOLDER_NAME);
    }

    public void inserts(List<EmrDataElementAdmin> data) {
        template.insert(data, EmrDataElementAdmin.class);
    }

    public void autoBind(Map<String, Object> data) {
        // {templateTrueName:"",dataSourceSet:[]}
        Update u = new Update();
        if (data != null && data.containsKey("dataSourceSet")) {
            u.set("dataSourceSet", data.get("dataSourceSet"));
        }
        template.upsert(
            Query.query(Criteria.where("templateTrueName").is(data.get("templateTrueName"))),
            u,
            "emrTemplateDatasource"
        );
        // template.insert(data,"emrTemplateDatasource");
    }

    public List<EmrDataElementAdmin> findByName(Set<String> name, String templateName) {
        return template.find(
            Query.query(
                Criteria.where("dataSourceName").in(name).and("templateName").is(templateName)
            ),
            EmrDataElementAdmin.class
        );
    }
}
