package com.hm.editor.adminservice.domain.datasourceManage.gateway;

import java.util.List;
import java.util.Map;

/**
 * 动态值域Gateway接口
 */
public interface DynamicDictGateway {

    /**
     * 新增动态值域
     */
    boolean addDynamicDict(Map<String, Object> data) throws Exception;

    /**
     * 编辑动态值域
     */
    boolean editorDynamicDict(Map<String, Object> data) throws Exception;

    /**
     * 删除动态值域（逻辑删除）
     */
    boolean delDynamicDict(String id);

    /**
     * 分页查询动态值域
     */
    Map<String, Object> getDynamicDict(String text, int pageNo, int pageSize);

    /**
     * 获取所有动态值域
     */
    List<Map<String, Object>> allDynamicDict();

    /**
     * 根据编码获取动态值域
     */
    Map<String, Object> getDynamicDictByCode(String code);

    /**
     * 根据编码获取动态值域实体
     */
    List<Map<String, Object>> findByCode(String code);
}
