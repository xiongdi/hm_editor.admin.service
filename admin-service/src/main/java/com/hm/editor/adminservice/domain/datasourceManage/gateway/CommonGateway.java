package com.hm.editor.adminservice.domain.datasourceManage.gateway;

import java.util.List;
import java.util.Map;

/**
 * 通用Gateway接口（提供通用数据访问能力）
 */
public interface CommonGateway {

    /**
     * 检查并保存数据
     */
    boolean checkAndSave(
        Map<String, Object> data,
        String colName,
        String uniqueKey,
        String... valueKey
    );

    /**
     * 删除数据
     */
    boolean delData(String id, String colName);

    /**
     * 分页查询数据
     */
    Map<String, Object> pageData(
        String colName,
        String txt,
        int pageNo,
        int pageSize,
        String sortKey,
        String... key
    );

    /**
     * 分页查询数据（使用Criteria）
     */
    Map<String, Object> pageData(
        String colName,
        Object criteria,
        int pageNo,
        int pageSize,
        String sortKey
    );

    /**
     * 获取所有引用
     */
    List<Map<String, Object>> getAllRef(String type, String... code);

    /**
     * 创建引用关系
     */
    boolean doRefence(String id, String type, String code, String refCode);

    /**
     * 创建引用关系（无ID）
     */
    boolean doRefence(String type, String code, String refCode);

    /**
     * 移除引用关系
     */
    boolean removeRefence(String type, String code, String refCode);

    /**
     * 检查是否可以删除
     */
    boolean canDel(String id, String type);

    /**
     * 获取引用列表
     */
    List<Map<String, Object>> refList(String type, String refCode);

    /**
     * 获取引用数量
     */
    long refCount(String type, String refCode);

    /**
     * 获取引用数量（多个编码）
     */
    List<Map<String, Object>> refCount(String type, List<String> refCode);
}
