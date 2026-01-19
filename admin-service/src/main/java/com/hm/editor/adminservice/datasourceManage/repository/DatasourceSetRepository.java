package com.hm.editor.adminservice.datasourceManage.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.hm.editor.adminservice.datasourceManage.utils.ContantUtil;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class DatasourceSetRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    public List<Map<String, Object>> getVerData(String dictVerId) {
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
        return mongoTemplate
            .aggregate(agg, ContantUtil.DS_REFRENCE_COLLECTION_NAME, Map.class)
            .getMappedResults();
    }

    public List<Map<String, Object>> getDsSetVerData(List<String> setCode) {
        return mongoTemplate.find(
            Query.query(Criteria.where("code").in(setCode).and("type").is("数据集")),
            Map.class,
            ContantUtil.DS_REFRENCE_COLLECTION_NAME
        );
    }
}
