package com.hm.editor.adminservice.infrastructure.persistence.datasourceManage;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.hm.editor.adminservice.domain.datasourceManage.gateway.CommonGateway;
import com.hm.editor.adminservice.domain.datasourceManage.gateway.DatasourceDictGateway;
import com.hm.editor.adminservice.infrastructure.utils.ContantUtil;
import com.hm.editor.adminservice.infrastructure.utils.DataUtil;
import com.hm.editor.adminservice.infrastructure.utils.HashIdGenerator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

/**
 * 数据源字典Gateway实现
 */
@Component
public class DatasourceDictGatewayImpl implements DatasourceDictGateway {

    private static final String dsDictColName = ContantUtil.DS_DICT_COLLECTION_NAME;
    private static final String dsDictVerDataColName =
        ContantUtil.DS_DICT_VARSION_DATA_COLLECTION_NAME;

    @Autowired
    private CommonGateway commonGateway;

    @Autowired
    private CommonGatewayImpl commonGatewayImpl;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean addDSDict(Map<String, Object> data) {
        data.remove("_id");
        return editorDSDict(data);
    }

    @Override
    public boolean editorDSDict(Map<String, Object> data) {
        String idKey = "_id";
        if (data.containsKey(idKey)) {
            data.put(idKey, DataUtil.str2ObjectId(data.get(idKey).toString()));
        }
        if (
            !data.containsKey("code") ||
            data.get("code") == null ||
            (data.get("code") instanceof String && ((String) data.get("code")).trim().isEmpty())
        ) {
            if (data.containsKey("name") && data.get("name") != null) {
                String name = data.get("name").toString();
                String code = HashIdGenerator.generate(name, 6);
                data.put("code", code);
            }
        }
        return commonGateway.checkAndSave(
            data,
            dsDictColName,
            "code",
            "code",
            "name",
            "status",
            "versionUid"
        );
    }

    @Override
    public boolean delDSDict(String id) {
        commonGateway.canDel(id, "值域");
        return commonGateway.delData(id, dsDictColName);
    }

    @Override
    public Map<String, Object> getDict(String text, int pageNo, int pageSize) {
        Map<String, Object> d = commonGateway.pageData(
            dsDictColName,
            text,
            pageNo,
            pageSize,
            "code",
            "code",
            "name"
        );
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> data = (List<Map<String, Object>>) d.get("data");
        final Map<String, Object> codeCount;
        if (!data.isEmpty()) {
            List<String> codes = data
                .stream()
                .map(_d -> _d.get("code").toString())
                .collect(Collectors.toList());

            List<Map<String, Object>> ref = commonGateway.refCount("数据元", codes);
            codeCount = ref
                .stream()
                .collect(
                    Collectors.toMap(_d -> _d.get("_id").toString(), _d -> _d.get("count"), (k1, k2) -> k1)
                );
        } else {
            codeCount = new HashMap<>();
        }

        data.forEach(_d -> {
            DataUtil.objectId2Str(_d);
            _d.put(
                "showStatus",
                (_d.get("status") != null && (boolean) _d.get("status")) ? "是" : "否"
            );
            _d.put("count", codeCount.getOrDefault(_d.get("code"), ""));
        });

        return d;
    }

    @Override
    public List<Map<String, Object>> allDict() {
        List<Map<String, Object>> d = commonGatewayImpl.find(
            dsDictColName,
            new Criteria(),
            Sort.by("code")
        );
        DataUtil.objectId2Str(d);
        return d;
    }

    @Override
    public List<Map<String, Object>> allUsedDict() {
        List<Map<String, Object>> d = commonGatewayImpl.find(
            dsDictColName,
            Criteria.where("status").is(true),
            Sort.by("code")
        );
        DataUtil.objectId2Str(d);
        return d;
    }

    @Override
    public List<Map<String, Object>> getDictVerData(String dictVerId) {
        List<Map<String, Object>> d = commonGatewayImpl.find(
            dsDictVerDataColName,
            Criteria.where("dictVersionUid").is(dictVerId),
            Sort.by("order")
        );
        DataUtil.objectId2Str(d, "_id");
        return d;
    }

    @Override
    public List<Map<String, Object>> editorDsDictVerData(Map<String, Object> d, String dictVerId) {
        d.put("dictVersionUid", dictVerId);
        commonGatewayImpl.save(
            d,
            dsDictVerDataColName,
            d.keySet().toArray(new String[d.size()])
        );
        return getDictVerData(dictVerId);
    }

    @Override
    public boolean delDsDictVerData(String id) {
        return commonGateway.delData(id, dsDictVerDataColName);
    }

    @Override
    public List<Map<String, Object>> getDsDictVerDataByCode(String code) {
        return getDictVerDataByCodes(code);
    }

    public List<Map<String, Object>> getDictVerDataByCodes(String... code) {
        Aggregation agg = newAggregation(
            match(Criteria.where("code").in(code)),
            lookup(
                ContantUtil.DS_DICT_VARSION_DATA_COLLECTION_NAME,
                "code",
                "dictVersionUid",
                "dictVer"
            ),
            unwind("dictVer"),
            project(
                "dictVer.val",
                "dictVer.calcVal",
                "dictVer.remark",
                "dictVer.description",
                "dictVer.order",
                "code"
            )
        ).withOptions(newAggregationOptions().allowDiskUse(true).build());

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> res = (List<Map<String, Object>>) (List<?>) mongoTemplate
            .aggregate(agg, ContantUtil.DS_DICT_COLLECTION_NAME, Map.class)
            .getMappedResults();
        return res;
    }

    @Override
    public List<Map<String, Object>> refData(String code) {
        String type = "数据元";
        return commonGateway.refList(type, code);
    }
}
