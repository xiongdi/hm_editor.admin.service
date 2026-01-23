package com.hm.editor.adminservice.infrastructure.persistence.console;

import com.hm.editor.adminservice.domain.console.model.EmrDataElementAdmin;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 数据元Repository接口
 */
@Repository
public interface DataElementRepository extends MongoRepository<EmrDataElementAdmin, String> {

    /**
     * 根据数据源名称模糊查询（忽略大小写）
     */
    Page<EmrDataElementAdmin> findByDataSourceNameContainingIgnoreCase(
        String dataSourceName,
        Pageable pageable
    );

    /**
     * 根据数据源名称和模板名称查询
     */
    @Query(
        "{ 'dataSourceName': { $regex: ?0, $options: 'i' }, 'templateName': { $regex: ?1 } }"
    )
    List<EmrDataElementAdmin> findByDataSourceNameAndTemplateName(
        String dataSourceName,
        String templateName
    );

    /**
     * 根据名称集合和模板名称查询
     */
    List<EmrDataElementAdmin> findByDataSourceNameInAndTemplateName(
        Set<String> names,
        String templateName
    );

    /**
     * 根据数据源名称统计
     */
    long countByDataSourceNameContainingIgnoreCase(String dataSourceName);

    /**
     * 根据数据源名称和模板名称统计
     */
    @Query(
        "{ 'dataSourceName': { $regex: ?0, $options: 'i' }, 'templateName': { $regex: ?1 } }"
    )
    long countByDataSourceNameAndTemplateName(String dataSourceName, String templateName);
}
