package com.hm.editor.adminservice.infrastructure.persistence.console;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.hm.editor.adminservice.domain.console.gateway.FolderTemplateGateway;
import com.hm.editor.adminservice.domain.console.model.EmrBaseTemplate;
import com.hm.editor.adminservice.infrastructure.utils.EscapeUtil;
import com.hm.editor.adminservice.infrastructure.utils.StringUtils;
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
import org.springframework.stereotype.Component;

/**
 * 模板管理Gateway实现
 */
@Component
public class FolderTemplateGatewayImpl implements FolderTemplateGateway {

    private static final String COLLECTION_NAME = "emrBaseTemplate";
    private static final String HTML_COLLECTION_NAME = "emrBaseTemplateHtml";

    @Autowired
    private FolderTemplateRepository folderTemplateRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public long getTotal(Map<String, Object> param) {
        return mongoTemplate.count(Query.query(getCri(param)), COLLECTION_NAME);
    }

    @Override
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
        return mongoTemplate
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

    @Override
    public boolean deleteById(String id) {
        if (id == null || !ObjectId.isValid(id)) {
            return false;
        }
        return (
            mongoTemplate
                .remove(Query.query(Criteria.where("_id").is(new ObjectId(id))), COLLECTION_NAME)
                .getDeletedCount() >
            0
        );
    }

    @Override
    public boolean update(EmrBaseTemplate emrBaseTemplate) {
        Query query = Query.query(Criteria.where("_id").is(emrBaseTemplate.get_id()));
        Update update = new Update();
        update.set("templateName", emrBaseTemplate.getTemplateName());
        update.set("isForbidMultiple", emrBaseTemplate.getIsForbidMultiple());
        update.set("type", emrBaseTemplate.getType());
        update.set("folderId", new ObjectId(emrBaseTemplate.getFolderStr()));
        update.set("editDate", emrBaseTemplate.getEditDate());
        update.set("createDate", emrBaseTemplate.getCreateDate());
        update.set("dsSet", emrBaseTemplate.getDsSet());
        if (emrBaseTemplate.getIsDefault() != null) {
            update.set("isDefault", emrBaseTemplate.getIsDefault());
        }
        mongoTemplate.upsert(query, update, COLLECTION_NAME);
        return true;
    }

    @Override
    public boolean existsByName(String id, String name) {
        return (
            mongoTemplate.findOne(
                Query.query(
                    Criteria.where("templateName").is(name).and("_id").ne(new ObjectId(id))
                ),
                EmrBaseTemplate.class
            ) !=
            null
        );
    }

    @Override
    public List<EmrBaseTemplate> findByCriteria(Criteria criteria) {
        return mongoTemplate.find(Query.query(criteria), EmrBaseTemplate.class, COLLECTION_NAME);
    }

    @Override
    public List<EmrBaseTemplate> findByFolderId(ObjectId folderId) {
        return folderTemplateRepository.findByFolderId(folderId);
    }

    @Override
    public List<Map<String, Object>> findAllWithFolder() {
        Aggregation agg = newAggregation(
            lookup("emrBaseFolder", "folderId", "_id", "folder"),
            unwind("folder"),
            project("templateName", "type", "isForbidMultiple", "folder.name")
        );
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> res = (List<Map<String, Object>>) (List<?>) mongoTemplate
            .aggregate(agg, COLLECTION_NAME, Map.class)
            .getMappedResults();
        return res;
    }

    @Override
    public boolean saveTemplateHtml(String id, String html) {
        if (id == null || !ObjectId.isValid(id)) {
            return false;
        }
        Query query = Query.query(Criteria.where("_id").is(new ObjectId(id)));
        Update update = new Update();
        update.set("html", html);
        mongoTemplate.upsert(query, update, HTML_COLLECTION_NAME);
        return true;
    }

    @Override
    public String getTemplateHtml(String id) {
        if (id == null || !ObjectId.isValid(id)) {
            return null;
        }

        ObjectId objectId = new ObjectId(id);
        Map<String, Object> templateMap = mongoTemplate.findOne(
            Query.query(Criteria.where("_id").is(objectId)),
            Map.class,
            HTML_COLLECTION_NAME
        );

        return templateMap != null ? (String) templateMap.get("html") : "";
    }

    @Override
    public boolean setDefaultTemplate(String templateId, boolean isDefault) {
        if (templateId == null || !ObjectId.isValid(templateId)) {
            return false;
        }

        ObjectId tid = new ObjectId(templateId);

        if (!isDefault) {
            mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(tid)),
                new Update().set("isDefault", "0"),
                COLLECTION_NAME
            );
            return true;
        }

        EmrBaseTemplate template = mongoTemplate.findOne(
            Query.query(Criteria.where("_id").is(tid)),
            EmrBaseTemplate.class,
            COLLECTION_NAME
        );
        if (template == null || template.getFolderId() == null) {
            return false;
        }

        ObjectId folderId = template.getFolderId();

        // 1) 取消该目录下原默认模板
        mongoTemplate.updateMulti(
            Query.query(Criteria.where("folderId").is(folderId).and("isDefault").is("1")),
            new Update().set("isDefault", "0"),
            COLLECTION_NAME
        );

        // 2) 设置当前模板为默认
        mongoTemplate.updateFirst(
            Query.query(Criteria.where("_id").is(tid)),
            new Update().set("isDefault", "1"),
            COLLECTION_NAME
        );

        return true;
    }
}
