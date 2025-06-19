package com.hm.editor.adminservice.datasourceManage.repository;

import com.hm.editor.adminservice.datasourceManage.utils.ContantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 动态值域数据库操作
 */
@Repository
public class DynamicDictRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 根据编码获取动态值域（只返回未删除的）
     *
     * @param code 编码
     * @return 动态值域
     */
    public List<Map> findByCode(String code) {
        Criteria criteria = Criteria.where("code").is(code);
        // 添加未删除条件（isDeleted为0或不存在）
        criteria.andOperator(
            new Criteria().orOperator(
                Criteria.where("isDeleted").is(0),
                Criteria.where("isDeleted").exists(false)
            )
        );

        Query query = new Query(criteria);
        return mongoTemplate.find(query, Map.class, ContantUtil.DYNAMIC_DICT_COLLECTION_NAME);
    }
}
