package com.hm.editor.adminservice.console.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.hm.editor.adminservice.console.domain.EmrBaseTemplate;
import com.hm.editor.common.utils.EscapeUtil;
import com.hm.editor.common.utils.StringUtils;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * @PROJECT_NAME:service_spce
 *
 * @author:wanglei
 * @date:2020/11/11 2:11 PM @Description:
 */
@Repository
public class FolderTemplateRepository {

    private static final String COLLECTION_NAME = "emrBaseTemplate";
    private static final String HTML_COLLECTION_NAME = "emrBaseTemplateHtml";

    @Autowired
    MongoTemplate template;

    public long getTotal(Map<String, Object> param) {
        return template.count(Query.query(getCri(param)), COLLECTION_NAME);
    }

    public List<EmrBaseTemplate> getAll(Map<String, Object> param) {
        Criteria match = getCri(param);
        Map<String, Integer> page = (Map<String, Integer>) param.get("page");
        int index = page.get("currentPage");
        int pageSize = page.get("pageSize");

        Aggregation aggregation = newAggregation(
            match(match),
            lookup("emrBaseFolder", "folderId", "_id", "folder"),
            unwind("folder"),
            sort(Sort.Direction.ASC, "createDate", "_id"),
            skip((index - 1) * pageSize),
            limit(pageSize)
        );
        return template
            .aggregate(aggregation, COLLECTION_NAME, EmrBaseTemplate.class)
            .getMappedResults();
    }

    private Criteria getCri(Map<String, Object> param) {
        Criteria c = new Criteria();
        String folderId = param.containsKey("folder") ? param.get("folder").toString() : "";
        String templateName = param.containsKey("templateName")
            ? param.get("templateName").toString()
            : "";
        if (StringUtils.isNotBlank(folderId)) {
            c.and("folderId").is(new ObjectId(folderId));
        }
        if (StringUtils.isNotBlank(templateName)) {
            c.and("templateName").regex(EscapeUtil.escapeExprSpecialWord(templateName));
        }
        return c;
    }

    public boolean delBasTemplate(String id) {
        return (
            template
                .remove(Query.query(Criteria.where("_id").is(new ObjectId(id))), COLLECTION_NAME)
                .getDeletedCount() >
            0
        );
    }

    public boolean editBasTemplate(EmrBaseTemplate emrBaseTemplate) {
        Query query = Query.query(Criteria.where("_id").is(emrBaseTemplate.get_id()));
        Update update = new Update();
        update.set("templateName", emrBaseTemplate.getTemplateName());
        update.set("isForbidMultiple", emrBaseTemplate.getIsForbidMultiple());
        update.set("type", emrBaseTemplate.getType());
        update.set("folderId", new ObjectId(emrBaseTemplate.getFolderStr()));
        update.set("editDate", emrBaseTemplate.getEditDate());
        update.set("createDate", emrBaseTemplate.getCreateDate());
        update.set("dsSet", emrBaseTemplate.getDsSet());
        template.upsert(query, update, COLLECTION_NAME);
        return true;
    }

    public boolean existOwnFolder(String id, String name) {
        return (
            template.findOne(
                Query.query(
                    Criteria.where("templateName").is(name).and("_id").ne(new ObjectId(id))
                ),
                EmrBaseTemplate.class
            ) !=
            null
        );
    }

    public List<EmrBaseTemplate> getListByCri(Criteria cri) {
        return template.find(Query.query(cri), EmrBaseTemplate.class, COLLECTION_NAME);
    }

    public List<EmrBaseTemplate> getListByFolderId(ObjectId id) {
        return getListByCri(Criteria.where("folderId").is(id));
    }

    public List<Map<String, Object>> allTemplate() {
        Aggregation agg = newAggregation(
            lookup("emrBaseFolder", "folderId", "_id", "folder"),
            unwind("folder"),
            project("templateName", "type", "isForbidMultiple", "folder.name")
        );
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> res = (List<Map<String, Object>>) (List<?>) template
            .aggregate(agg, "emrBaseTemplate", Map.class)
            .getMappedResults();
        return res;
    }

    public boolean saveBaseTemplateHtml(String id, String html) {
        Query query = Query.query(Criteria.where("_id").is(new ObjectId(id)));
        Update update = new Update();
        update.set("html", html);
        template.upsert(query, update, HTML_COLLECTION_NAME);
        return true;
    }

    public String getBaseTemplateHtml(String id) {
        if (id == null || !ObjectId.isValid(id)) {
            return null;
        }

        ObjectId objectId = new ObjectId(id);
        Map<String, Object> templateMap = template.findOne(
            Query.query(Criteria.where("_id").is(objectId)),
            Map.class,
            HTML_COLLECTION_NAME
        );

        return templateMap != null ? (String) templateMap.get("html") : "";
    }
}
