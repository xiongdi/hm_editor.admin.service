package com.hm.editor.adminservice.infrastructure.persistence.datasourceManage;

import com.hm.editor.adminservice.domain.datasourceManage.gateway.CommonGateway;
import com.hm.editor.adminservice.domain.datasourceManage.gateway.DynamicDictGateway;
import com.hm.editor.adminservice.infrastructure.utils.ContantUtil;
import com.hm.editor.adminservice.infrastructure.utils.DataUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * 动态值域Gateway实现
 */
@Component
public class DynamicDictGatewayImpl implements DynamicDictGateway {

    private static final String dynamicDictColName = ContantUtil.DYNAMIC_DICT_COLLECTION_NAME;

    @Autowired
    private CommonGateway commonGateway;

    @Autowired
    private CommonGatewayImpl commonGatewayImpl;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean addDynamicDict(Map<String, Object> data) throws Exception {
        data.remove("_id");
        return editorDynamicDict(data);
    }

    @Override
    public boolean editorDynamicDict(Map<String, Object> data) throws Exception {
        String idKey = "_id";

        if (!data.containsKey(idKey)) {
            String newCode = generateNewDictCode();
            data.put("code", newCode);
            data.put("isDeleted", 0);
        }

        if (data.containsKey(idKey)) {
            data.put(idKey, DataUtil.str2ObjectId(data.get(idKey).toString()));
        }
        data.put("createTime", new Date());
        data.put("updateTime", new Date());

        return commonGateway.checkAndSave(
            data,
            dynamicDictColName,
            "code",
            "code",
            "name",
            "tag",
            "url",
            "paramExample",
            "returnCode",
            "returnName",
            "returnExt1",
            "returnExt2",
            "displayContent",
            "createTime",
            "updateTime",
            "isDeleted"
        );
    }

    @Override
    public boolean delDynamicDict(String id) {
        Query query = new Query(Criteria.where("_id").is(DataUtil.str2ObjectId(id)));
        Update update = new Update().set("isDeleted", 1).set("updateTime", new Date());
        return mongoTemplate.updateFirst(query, update, dynamicDictColName).getModifiedCount() > 0;
    }

    @Override
    public Map<String, Object> getDynamicDict(String text, int pageNo, int pageSize) {
        Criteria criteria = new Criteria();

        criteria.orOperator(
            Criteria.where("isDeleted").is(0),
            Criteria.where("isDeleted").exists(false)
        );

        Criteria searchCriteria = new Criteria();
        searchCriteria.orOperator(
            Criteria.where("code").regex(text, "i"),
            Criteria.where("name").regex(text, "i"),
            Criteria.where("tag").regex(text, "i")
        );

        criteria.andOperator(searchCriteria);

        Map<String, Object> d = commonGateway.pageData(
            dynamicDictColName,
            criteria,
            pageNo,
            pageSize,
            "code"
        );
        List<Map<String, Object>> data = (List<Map<String, Object>>) d.get("data");

        data.forEach(DataUtil::objectId2Str);

        return d;
    }

    @Override
    public List<Map<String, Object>> allDynamicDict() {
        Criteria criteria = new Criteria();
        criteria.orOperator(
            Criteria.where("isDeleted").is(0),
            Criteria.where("isDeleted").exists(false)
        );

        List<Map<String, Object>> d = commonGatewayImpl.find(
            dynamicDictColName,
            criteria,
            Sort.by("code")
        );
        DataUtil.objectId2Str(d);
        return d;
    }

    @Override
    public Map<String, Object> getDynamicDictByCode(String code) {
        Criteria criteria = Criteria.where("code").is(code);
        criteria.andOperator(
            new Criteria().orOperator(
                Criteria.where("isDeleted").is(0),
                Criteria.where("isDeleted").exists(false)
            )
        );

        Map<String, Object> d = commonGatewayImpl.findOne(dynamicDictColName, criteria);
        if (d != null) {
            DataUtil.objectId2Str(d);
        }
        return d;
    }

    @Override
    public List<Map<String, Object>> findByCode(String code) {
        Criteria criteria = Criteria.where("code").is(code);
        criteria.andOperator(
            new Criteria().orOperator(
                Criteria.where("isDeleted").is(0),
                Criteria.where("isDeleted").exists(false)
            )
        );

        Query query = new Query(criteria);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> result =
            (List<Map<String, Object>>) (List<?>) mongoTemplate.find(
                query,
                Map.class,
                dynamicDictColName
            );
        return result;
    }

    private String generateNewDictCode() throws Exception {
        List<Map<String, Object>> dicts = commonGatewayImpl.find(
            dynamicDictColName,
            Criteria.where("code").regex("^DC\\d+\\.\\d+"),
            Sort.by(Sort.Direction.DESC, "code")
        );

        int majorNum = 0;
        int minorNum = 0;

        if (!dicts.isEmpty()) {
            String lastCode = (String) dicts.get(0).get("code");
            try {
                if (
                    lastCode.length() >= 8 && lastCode.startsWith("DC") && lastCode.charAt(4) == '.'
                ) {
                    String majorPart = lastCode.substring(2, 4);
                    String minorPart = lastCode.substring(5, 8);

                    majorNum = Integer.parseInt(majorPart);
                    minorNum = Integer.parseInt(minorPart);

                    minorNum++;
                    if (minorNum > 999) {
                        majorNum++;
                        minorNum = 0;
                    }

                    if (majorNum > 99) {
                        throw new Exception("值域编码已达到最大值DC99.999，无法继续生成");
                    }
                }
            } catch (NumberFormatException e) {
                majorNum = 0;
                minorNum = 1;
            }
        } else {
            minorNum = 1;
        }

        return String.format("DC%02d.%03d", majorNum, minorNum);
    }
}
