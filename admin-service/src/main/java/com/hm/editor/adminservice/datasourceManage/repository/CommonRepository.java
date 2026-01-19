package com.hm.editor.adminservice.datasourceManage.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.hm.editor.adminservice.datasourceManage.utils.DataUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class CommonRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    public boolean exists(Criteria cri, String colName) {
        return mongoTemplate.exists(Query.query(cri), colName);
    }

    public long count(Criteria cri, String colName) {
        return mongoTemplate.count(Query.query(cri), colName);
    }

    public List<Map<String, Object>> find(String colName, Criteria cri, Sort sort) {
        Query q = Query.query(cri);
        if (sort != null) {
            q.with(sort);
        }
        return mongoTemplate.find(q, Map.class, colName);
    }

    public Map<String, Object> findOne(String colName, Criteria cri) {
        Query q = Query.query(cri);

        return mongoTemplate.findOne(q, Map.class, colName);
    }

    public List<Map<String, Object>> pageData(String colName, Criteria cri, Sort sort, int pageNo, int pageSize) {
        Aggregation agg = Aggregation.newAggregation(
            Aggregation.match(cri),
            Aggregation.sort(sort),
            Aggregation.skip((pageNo - 1) * pageSize),
            Aggregation.limit(pageNo * pageSize)
        ).withOptions(newAggregationOptions().allowDiskUse(true).build());
        return mongoTemplate.aggregate(agg, colName, Map.class).getMappedResults();
    }

    public void insert(Map<String, Object> d, String colName) {
        mongoTemplate.insert(d, colName);
    }

    public void inserts(List<Map<String, Object>> d, String colName) {
        BulkOperations bulkOperations = mongoTemplate.bulkOps(
            BulkOperations.BulkMode.UNORDERED,
            colName
        );
        bulkOperations.insert(d);
        bulkOperations.execute();
    }

    public void updateOne(Criteria cri, Update update, String colName) {
        mongoTemplate.updateFirst(Query.query(cri), update, colName);
    }

    public void updateOrInsert(Criteria cri, Update update, String colName) {
        mongoTemplate.upsert(Query.query(cri), update, colName);
    }

    public void updateAll(Criteria cri, Update update, String colName) {
        mongoTemplate.updateMulti(Query.query(cri), update, colName);
    }

    public boolean delData(ObjectId id, String colName) {
        return delData(Criteria.where("_id").is(id), colName);
    }

    public boolean delData(Criteria cri, String colName) {
        mongoTemplate.remove(Query.query(cri), colName);
        return true;
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
            insert(i, colName);
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
            updateOne(cri, u, colName);
        }
    }

    public List<Map<String, Object>> agg(List<AggregationOperation> aggs, String name) {
        Aggregation _agg = newAggregation(aggs).withOptions(
            newAggregationOptions().allowDiskUse(true).build()
        );
        return mongoTemplate.aggregate(_agg, name, Map.class).getMappedResults();
    }

    public Object maxVal(String name, Criteria criteria, String fieldName) {
        Aggregation agg = newAggregation(
            match(criteria),
            sort(Sort.Direction.DESC, fieldName),
            limit(1)
        ).withOptions(newAggregationOptions().allowDiskUse(true).build());

        List<Map<String, Object>> r = mongoTemplate.aggregate(agg, name, Map.class).getMappedResults();
        Map<String, Object> m;
        if (r.isEmpty() || (m = r.get(0)) == null) {
            return null;
        }
        return m.get(fieldName);
    }
}
