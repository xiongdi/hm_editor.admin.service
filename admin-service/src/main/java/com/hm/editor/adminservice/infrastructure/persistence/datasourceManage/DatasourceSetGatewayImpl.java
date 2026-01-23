package com.hm.editor.adminservice.infrastructure.persistence.datasourceManage;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.hm.editor.adminservice.domain.datasourceManage.gateway.CommonGateway;
import com.hm.editor.adminservice.domain.datasourceManage.gateway.DatasourceSetGateway;
import com.hm.editor.adminservice.infrastructure.utils.ContantUtil;
import com.hm.editor.adminservice.infrastructure.utils.DataUtil;
import com.hm.editor.adminservice.infrastructure.exception.BusinessException;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

/**
 * 数据集Gateway实现
 */
@Component
public class DatasourceSetGatewayImpl implements DatasourceSetGateway {

    private static final String colName = ContantUtil.DS_SET_COLLECTION_NAME;

    @Autowired
    private CommonGateway commonGateway;

    @Autowired
    private CommonGatewayImpl commonGatewayImpl;

    @Autowired
    private DatasourceDictGatewayImpl datasourceDictGatewayImpl;

    @Autowired
    private MongoTemplate mongoTemplate;

    private List<Map<String, Object>> getDsSetVerData(List<String> setCode) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> res = (List<Map<String, Object>>) (List<?>) mongoTemplate.find(
            Query.query(Criteria.where("code").in(setCode).and("type").is("数据集")),
            Map.class,
            ContantUtil.DS_REFRENCE_COLLECTION_NAME
        );
        return res;
    }

    @Override
    public Map<String, Object> addData(Map<String, Object> data) {
        data.remove("_id");
        return editorData(data);
    }

    @Override
    public Map<String, Object> editorData(Map<String, Object> data) {
        String idKey = "_id";
        boolean addFlag = !data.containsKey(idKey);
        if (!addFlag) {
            data.put(idKey, DataUtil.str2ObjectId(data.get(idKey).toString()));
        }
        boolean flag = commonGateway.checkAndSave(
            data,
            colName,
            "code",
            "code",
            "name",
            "versionUid"
        );
        Map<String, Object> res = new HashMap<>();
        res.put("editFlag", flag);
        if (flag && addFlag) {
            List<Map<String, Object>> all = commonGatewayImpl.find(
                colName,
                new Criteria(),
                Sort.by(Sort.Direction.ASC, "code")
            );
            int addIndex = 0;
            for (Map<String, Object> m : all) {
                if (data.get("code").equals(m.get("code"))) {
                    res.put("index", addIndex);
                    DataUtil.objectId2StrByKey(m, "_id");
                    res.put("addData", m);
                }
                addIndex++;
            }
        }
        return res;
    }

    @Override
    public boolean delData(String id) {
        commonGateway.canDel(id, "数据集");
        Map<String, Object> s = commonGatewayImpl.findOne(
            colName,
            Criteria.where("_id").is(DataUtil.str2ObjectId(id))
        );
        List<Map<String, Object>> d = commonGatewayImpl.find(
            "emrBaseTemplate",
            Criteria.where("dsSet").is(s.get("code")),
            null
        );
        if (!d.isEmpty()) {
            throw new BusinessException(
                "数据集已被【" + d.get(0).get("templateName") + "】引用，请先取消引用"
            );
        }
        commonGateway.delData(id, colName);
        if (s != null && s.get("code") != null) {
            commonGatewayImpl.delData(
                Criteria.where("code").is(s.get("code")),
                ContantUtil.DS_REFRENCE_COLLECTION_NAME
            );
        }
        return true;
    }

    @Override
    public Map<String, Object> getData(String text, int pageNo, int pageSize) {
        Map<String, Object> d = commonGateway.pageData(
            colName,
            text,
            pageNo,
            pageSize,
            "code",
            "code",
            "name"
        );
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> data = (List<Map<String, Object>>) d.get("data");
        Set<String> codes = data
            .stream()
            .filter(_d -> _d.get("code") != null && !_d.get("code").toString().isEmpty())
            .map(_d -> _d.get("code").toString())
            .collect(Collectors.toSet());
        Map<String, List<Map<String, Object>>> codeRef = refData(codes);

        data.forEach(_d -> {
            DataUtil.objectId2Str(_d);
            _d.put(
                "count",
                codeRef.containsKey(_d.get("code")) ? codeRef.get(_d.get("code")).size() : ""
            );
        });

        return d;
    }

    @Override
    public Map<String, Object> getVerData(String dictVerId) {
        Map<String, Object> res = new HashMap<>();
        List<Map<String, Object>> d = getVerDataInternal(dictVerId);
        d = d
            .stream()
            .filter(_d -> _d.containsKey("gp") || _d.containsKey("ds"))
            .collect(Collectors.toList());
        List<String> gcode = new ArrayList<>();
        for (Map<String, Object> dd : d) {
            DataUtil.objectId2Str(dd);
            Object l = dd.get("ds");
            if (l != null && l instanceof Map && !((Map<?, ?>) l).isEmpty()) {
                dd.put("name", ((Map<?, ?>) l).get("name"));
            }
        }

        res.put("data", d);
        return res;
    }

    private List<Map<String, Object>> getVerDataInternal(String dictVerId) {
        Fields fields = Fields.from(
            Fields.field("_id"),
            Fields.field("type"),
            Fields.field("code"),
            Fields.field("refCode"),
            Fields.field("remark"),
            Fields.field("ds"),
            Fields.field("gp")
        );
        Criteria cri = Criteria.where("code").is(dictVerId).and("type").is("数据集");
        Aggregation agg = newAggregation(
            match(cri),
            lookup(ContantUtil.DS_COLLECTION_NAME, "refCode", "code", "ds"),
            unwind("ds", true),
            project(fields)
        ).withOptions(newAggregationOptions().allowDiskUse(true).build());
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> res = (List<Map<String, Object>>) (List<?>) mongoTemplate
            .aggregate(agg, ContantUtil.DS_REFRENCE_COLLECTION_NAME, Map.class)
            .getMappedResults();
        return res;
    }

    @Override
    public Map<String, Object> editorVerData(Map<String, Object> d, String dictVerId) {
        Map<String, Object> d1 = new HashMap<>();
        d1.put("_id", d.get("_id"));
        d1.put("type", "数据集");
        d1.put("code", dictVerId);
        d1.put("refCode", d.get("code"));
        d1.put("remark", d.get("type"));
        d1.put("createTime", new Date());
        commonGatewayImpl.save(
            d1,
            ContantUtil.DS_REFRENCE_COLLECTION_NAME,
            d1.keySet().toArray(new String[d.size()])
        );
        return getVerData(dictVerId);
    }

    @Override
    public boolean delDictVerData(String id) {
        return commonGateway.delData(
            id,
            ContantUtil.DS_REFRENCE_COLLECTION_NAME
        );
    }

    @Override
    public List<Map<String, Object>> allPublishedDsSet() {
        List<Map<String, Object>> data = commonGatewayImpl.find(colName, new Criteria(), null);

        DataUtil.objectId2Str(data);
        return data;
    }

    @Override
    public List<Map<String, Object>> allDsList(List<String> setCode) {
        List<Map<String, Object>> l = getDsSetVerData(setCode);

        Set<String> dsCode = l
            .stream()
            .filter(ll -> "数据元".equals(ll.get("remark")))
            .map(ll -> ll.get("refCode").toString())
            .collect(Collectors.toSet());
        Set<String> allDsCode = new HashSet<>();

        if (!dsCode.isEmpty()) {
            allDsCode.addAll(dsCode);
        }
        if (allDsCode.isEmpty()) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> _ds = commonGatewayImpl.find(
            ContantUtil.DS_COLLECTION_NAME,
            Criteria.where("code").in(allDsCode),
            null
        );
        Set<String> dictCode = _ds
            .stream()
            .filter(d -> d.get("dictCode") != null && !d.get("dictCode").toString().isEmpty())
            .map(d -> d.get("dictCode").toString())
            .collect(Collectors.toSet());
        if (!dictCode.isEmpty()) {
            List<Map<String, Object>> dicts = datasourceDictGatewayImpl.getDictVerDataByCodes(
                dictCode.toArray(new String[dictCode.size()])
            );
            Map<String, List<Map<String, Object>>> dg = dicts
                .stream()
                .collect(Collectors.groupingBy(d -> d.get("code").toString()));

            _ds.forEach(_d -> {
                if (_d.containsKey("dictCode") && dg.containsKey(_d.get("dictCode"))) {
                    List<Map<String, Object>> dictList = dg.get(_d.get("dictCode"));
                    dictList.forEach(dict -> {
                        if (dict.containsKey("description")) {
                            dict.put("code", dict.get("description"));
                        }
                    });
                    _d.put("dictList", dictList);
                }
            });
        }
        return _ds;
    }

    @Override
    public List<Map<String, Object>> refData(String code) {
        List<Map<String, Object>> d = commonGatewayImpl.find(
            "emrBaseTemplate",
            Criteria.where("dsSet").is(code),
            null
        );

        d.forEach(_d -> _d.put("name", _d.get("templateName")));
        return d;
    }

    @Override
    public Map<String, List<Map<String, Object>>> refData(Set<String> code) {
        Map<String, List<Map<String, Object>>> groupByCode = new HashMap<>();
        if (code == null || code.isEmpty()) {
            return groupByCode;
        }
        List<Map<String, Object>> d = commonGatewayImpl.find(
            "emrBaseTemplate",
            Criteria.where("dsSet").in(code),
            null
        );
        d.forEach(_d -> _d.put("name", _d.get("templateName")));

        for (Map<String, Object> dd : d) {
            Object dsSet = dd.get("dsSet");
            if (dsSet != null && dsSet instanceof Collection) {
                @SuppressWarnings("unchecked")
                Collection<String> _dses = (Collection<String>) dsSet;
                Set<String> dses = new HashSet<>(_dses);
                for (String ds : dses) {
                    List<Map<String, Object>> dsL = groupByCode.getOrDefault(ds, new ArrayList<>());
                    dsL.add(dd);
                    groupByCode.put(ds, dsL);
                }
            }
        }

        return groupByCode;
    }
}
