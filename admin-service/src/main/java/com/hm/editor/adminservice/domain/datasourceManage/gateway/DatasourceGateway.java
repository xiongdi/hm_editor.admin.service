package com.hm.editor.adminservice.domain.datasourceManage.gateway;

import java.util.List;
import java.util.Map;

/**
 * 数据源Gateway接口
 */
public interface DatasourceGateway {

    /**
     * 新增数据源
     */
    boolean addDS(Map<String, Object> data);

    /**
     * 编辑数据源
     */
    boolean editorDS(Map<String, Object> data);

    /**
     * 删除数据源
     */
    boolean delDs(String id);

    /**
     * 分页查询数据源
     */
    Map<String, Object> getDs(String text, int pageNo, int pageSize);

    /**
     * 获取所有数据源
     */
    List<Map<String, Object>> getAllDs();

    /**
     * 获取引用数据
     */
    List<Map<String, Object>> refData(String code);

    /**
     * 生成代码
     */
    String generalCode(String name);
}
