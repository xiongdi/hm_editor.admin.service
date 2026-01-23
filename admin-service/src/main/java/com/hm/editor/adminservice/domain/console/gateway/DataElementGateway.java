package com.hm.editor.adminservice.domain.console.gateway;

import com.hm.editor.adminservice.domain.console.model.EmrDataElementAdmin;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据元Gateway接口
 */
public interface DataElementGateway {

    /**
     * 分页查询数据元
     */
    List<EmrDataElementAdmin> findAll(Map<String, Object> param);

    /**
     * 统计总数
     */
    long count(Map<String, Object> param);

    /**
     * 根据ID删除
     */
    boolean deleteById(String id);

    /**
     * 更新数据元
     */
    boolean update(Map<String, Object> param);

    /**
     * 插入数据元
     */
    boolean insert(Map<String, Object> param);

    /**
     * 搜索数据元
     */
    List<Map<String, Object>> searchDataElement(String dataSourceName, String templateName);

    /**
     * 获取所有数据
     */
    List<EmrDataElementAdmin> getAllData();

    /**
     * 批量插入
     */
    void batchInsert(List<EmrDataElementAdmin> data);

    /**
     * 自动绑定模板数据源
     */
    void autoBind(Map<String, Object> templateDataSource);

    /**
     * 根据名称和模板名称查询
     */
    List<EmrDataElementAdmin> findByName(Set<String> name, String templateName);
}
