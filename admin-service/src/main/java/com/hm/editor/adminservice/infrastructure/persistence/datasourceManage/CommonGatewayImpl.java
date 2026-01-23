package com.hm.editor.adminservice.infrastructure.persistence.datasourceManage;

import com.hm.editor.adminservice.domain.datasourceManage.gateway.CommonGateway;
import com.hm.editor.adminservice.infrastructure.utils.ContantUtil;
import com.hm.editor.adminservice.infrastructure.utils.DataUtil;
import com.hm.editor.adminservice.infrastructure.utils.EscapeUtil;
import com.hm.editor.adminservice.infrastructure.utils.StringUtils;
import com.hm.editor.adminservice.infrastructure.exception.BusinessException;
import java.util.*;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * 通用Gateway实现
 */
@Component
public class CommonGatewayImpl implements CommonGateway {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean checkAndSave(
        Map<String, Object> d,
        String colName,
        String uniqueKey,
        String... valueKey
    ) {
        if (d == null || d.isEmpty()) {
            throw new BusinessException("数据异常");
        }

        Object _id = d.get("_id");
        Criteria cri = Criteria.where(uniqueKey).is(d.get(uniqueKey));
        if (_id != null) {
            cri.and("_id").ne(_id);
        }
        if (mongoTemplate.exists(Query.query(cri), colName)) {
            throw new BusinessException("存在【" + d.get(valueKey[0]) + "】的数据");
        }

        save(d, colName, valueKey);
        return true;
    }

    @Override
    public boolean delData(String id, String colName) {
        return delData(DataUtil.str2ObjectId(id), colName);
    }

    private boolean delData(ObjectId id, String colName) {
        mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), colName);
        return true;
    }

    @Override
    public Map<String, Object> pageData(
        String colName,
        String txt,
        int pageNo,
        int pageSize,
        String sortKey,
        String... key
    ) {
        Map<String, Object> res = new HashMap<>();
        Criteria criteria = new Criteria();
        if (StringUtils.isNotBlank(txt)) {
            criteria.orOperator(
                Arrays.stream(key)
                    .map(s -> Criteria.where(s).regex(EscapeUtil.escapeExprSpecialWord(txt)))
                    .collect(Collectors.toList())
            );
        }
        res.put("total", mongoTemplate.count(Query.query(criteria), colName));
        List<Map<String, Object>> data = new ArrayList<>();
        if ((long) res.get("total") > 0) {
            Sort sort = null;
            if (StringUtils.isNotBlank(sortKey)) {
                sort = Sort.by(sortKey).descending();
            }
            data = pageData(colName, criteria, sort, pageNo, pageSize);
        }

        res.put("data", data);
        return res;
    }

    @Override
    public Map<String, Object> pageData(
        String colName,
        Object criteriaObj,
        int pageNo,
        int pageSize,
        String sortKey
    ) {
        Map<String, Object> res = new HashMap<>();
        Criteria criteria = (Criteria) criteriaObj;
        res.put("total", mongoTemplate.count(Query.query(criteria), colName));
        List<Map<String, Object>> data = new ArrayList<>();
        if ((long) res.get("total") > 0) {
            Sort sort = null;
            if (StringUtils.isNotBlank(sortKey)) {
                sort = Sort.by(sortKey);
            }
            data = pageData(colName, criteria, sort, pageNo, pageSize);
        }

        res.put("data", data);
        return res;
    }

    private List<Map<String, Object>> pageData(
        String colName,
        Criteria cri,
        Sort sort,
        int pageNo,
        int pageSize
    ) {
        Aggregation agg = Aggregation.newAggregation(
            Aggregation.match(cri),
            Aggregation.sort(sort != null ? sort : Sort.by(Sort.Direction.ASC, "_id")),
            Aggregation.skip((pageNo - 1) * pageSize),
            Aggregation.limit(pageSize)
        ).withOptions(
            Aggregation.newAggregationOptions().allowDiskUse(true).build()
        );
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> res = (List<Map<String, Object>>) (List<?>) mongoTemplate
            .aggregate(agg, colName, Map.class)
            .getMappedResults();
        return res;
    }

    @Override
    public List<Map<String, Object>> getAllRef(String type, String... code) {
        Criteria cri = Criteria.where("type").is(type).and("code").in(code);
        String name = "";
        if ("数据集".equals(type)) {
            name = ContantUtil.DS_COLLECTION_NAME;
        } else if ("数据元".equals(type)) {
            name = ContantUtil.DS_DICT_COLLECTION_NAME;
        }
        return getOneGroupRef(cri, name);
    }

    private List<Map<String, Object>> getOneGroupRef(Criteria cri, String refName) {
        List<AggregationOperation> aggs = new ArrayList<>();
        aggs.add(Aggregation.match(cri));

        aggs.add(Aggregation.lookup(refName, "refCode", "code", "r1"));
        aggs.add(Aggregation.unwind("r1"));
        aggs.add(Aggregation.sort(Sort.Direction.ASC, "createTime"));
        aggs.add(Aggregation.project("_id", "code", "refCode", "r1.name"));
        aggs.add(Aggregation.match(Criteria.where("name").ne(null)));
        return agg(aggs, ContantUtil.DS_REFRENCE_COLLECTION_NAME);
    }

    @Override
    public boolean doRefence(String id, String type, String code, String refCode) {
        Map<String, Object> d = new HashMap<>();
        d.put("_id", DataUtil.str2ObjectId(id));
        d.put("type", type);
        d.put("code", code);
        d.put("refCode", refCode);
        d.put("createTime", new Date());
        save(
            d,
            ContantUtil.DS_REFRENCE_COLLECTION_NAME,
            "type",
            "code",
            "refCode",
            "createTime"
        );
        return true;
    }

    @Override
    public boolean doRefence(String type, String code, String refCode) {
        Criteria cri = Criteria.where("type").is(type).and("code").is(code);
        Update u = new Update();
        u.set("refCode", refCode);
        u.set("createTime", new Date());
        mongoTemplate.upsert(Query.query(cri), u, ContantUtil.DS_REFRENCE_COLLECTION_NAME);
        return true;
    }

    @Override
    public boolean removeRefence(String type, String code, String refCode) {
        Criteria cri = Criteria.where("type").is(type).and("code").is(code);
        if (!StringUtils.isEmpty(refCode)) {
            cri.and("refCode").is(refCode);
        }
        mongoTemplate.remove(Query.query(cri), ContantUtil.DS_REFRENCE_COLLECTION_NAME);
        return true;
    }

    @Override
    public boolean canDel(String id, String type) {
        // TODO: 实现删除检查逻辑
        return true;
    }

    @Override
    public List<Map<String, Object>> refList(String type, String refCode) {
        String refName = "";
        if ("数据元".equals(type)) {
            refName = ContantUtil.DS_COLLECTION_NAME;
        } else if ("数据集".equals(type)) {
            refName = ContantUtil.DS_SET_COLLECTION_NAME;
        }
        Criteria cri = Criteria.where("type").is(type).and("refCode").is(refCode);
        List<AggregationOperation> aggs = new ArrayList<>();
        aggs.add(Aggregation.match(cri));

        aggs.add(Aggregation.lookup(refName, "code", "code", "r1"));
        aggs.add(Aggregation.unwind("r1"));
        aggs.add(Aggregation.sort(Sort.Direction.ASC, "createTime"));
        aggs.add(Aggregation.project("r1.code", "r1.name"));
        List<Map<String, Object>> d = agg(aggs, ContantUtil.DS_REFRENCE_COLLECTION_NAME);
        return d
            .stream()
            .filter(_d -> _d.get("name") != null && !_d.get("name").toString().isEmpty())
            .collect(Collectors.toList());
    }

    @Override
    public long refCount(String type, String refCode) {
        return mongoTemplate.count(
            Query.query(Criteria.where("type").is(type).and("refCode").is(refCode)),
            ContantUtil.DS_REFRENCE_COLLECTION_NAME
        );
    }

    @Override
    public List<Map<String, Object>> refCount(String type, List<String> refCode) {
        Criteria cri = Criteria.where("type").is(type).and("refCode").in(refCode);
        List<AggregationOperation> aggs = new ArrayList<>();
        aggs.add(Aggregation.match(cri));
        aggs.add(Aggregation.group("refCode").count().as("count"));
        return agg(aggs, ContantUtil.DS_REFRENCE_COLLECTION_NAME);
    }

    private List<Map<String, Object>> agg(List<AggregationOperation> aggs, String name) {
        Aggregation _agg = Aggregation.newAggregation(aggs).withOptions(
            Aggregation.newAggregationOptions().allowDiskUse(true).build()
        );
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> res = (List<Map<String, Object>>) (List<?>) mongoTemplate
            .aggregate(_agg, name, Map.class)
            .getMappedResults();
        return res;
    }

    public void save(Map<String, Object> d, String colName, String... updateKeys) {
        ObjectId id = null;
        if (d.containsKey("_id") && d.get("_id") != null) {
            if (d.get("_id") instanceof ObjectId) {
                id = (ObjectId) d.get("_id");
            } else {
                id = DataUtil.str2ObjectId(d.get("_id").toString());
            }
        }
        if (id == null) {
            Map<String, Object> i = new HashMap<>();
            for (String k : updateKeys) {
                Object v = d.get(k);
                if (v != null) {
                    i.put(k, v);
                }
            }
            mongoTemplate.insert(i, colName);
        } else {
            Criteria cri = Criteria.where("_id").is(id);
            Update u = new Update();
            for (String k : updateKeys) {
                Object v = d.get(k);
                if (v != null) {
                    u.set(k, v);
                } else {
                    u.unset(k);
                }
            }
            mongoTemplate.updateFirst(Query.query(cri), u, colName);
        }
    }

    public Object maxVal(String name, Criteria criteria, String fieldName) {
        Aggregation agg = Aggregation.newAggregation(
            Aggregation.match(criteria),
            Aggregation.sort(Sort.Direction.DESC, fieldName),
            Aggregation.limit(1)
        ).withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> r = (List<Map<String, Object>>) (List<?>) mongoTemplate
            .aggregate(agg, name, Map.class)
            .getMappedResults();
        Map<String, Object> m;
        if (r.isEmpty() || (m = r.get(0)) == null) {
            return null;
        }
        return m.get(fieldName);
    }

    public Map<String, Object> findOne(String colName, Criteria cri) {
        return mongoTemplate.findOne(Query.query(cri), Map.class, colName);
    }

    public List<Map<String, Object>> find(String colName, Criteria cri, Sort sort) {
        Query q = Query.query(cri);
        if (sort != null) {
            q.with(sort);
        }
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> res = (List<Map<String, Object>>) (List<?>) mongoTemplate.find(
            q,
            Map.class,
            colName
        );
        return res;
    }

    public long count(Criteria cri, String colName) {
        return mongoTemplate.count(Query.query(cri), colName);
    }

    public boolean exists(Criteria cri, String colName) {
        return mongoTemplate.exists(Query.query(cri), colName);
    }

    public void delData(Criteria cri, String colName) {
        mongoTemplate.remove(Query.query(cri), colName);
    }
}
