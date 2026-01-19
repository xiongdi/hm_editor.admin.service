package com.hm.editor.adminservice.datasourceManage.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.hm.editor.adminservice.datasourceManage.utils.ContantUtil;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
public class DatasourceDictRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    public List<Map<String, Object>> getDictVerDataByCode(String code) {
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
        //        Aggregation agg = newAggregation(
        //                match(Criteria.where("code").is(code)),
        //
        // lookup(ContantUtil.DS_DICT_VERSION_COLLECTION_NAME,"code","dictCode","dictVer"),
        //                unwind("dictVer"),
        //                match(Criteria.where("dictVer.publishTime").exists(true)),
        //                sort(Sort.by(Sort.Direction.DESC,"dictVer.createTime")),
        //                group("dictVer._id").first("dictVer.uid").as("uid"),
        //
        // lookup(ContantUtil.DS_DICT_VARSION_DATA_COLLECTION_NAME,"uid","dictVersionUid","verData"),
        //
        // project("verData.val","verData.calcVal","verData.remark","verData.description","verData.order")
        //
        //        ).withOptions(newAggregationOptions().allowDiskUse(true).build());

        return mongoTemplate
            .aggregate(agg, ContantUtil.DS_DICT_COLLECTION_NAME, Map.class)
            .getMappedResults();
    }
}
