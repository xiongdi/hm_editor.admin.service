package com.hm.editor.adminservice.domain.datasourceManage.gateway;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据集Gateway接口
 */
public interface DatasourceSetGateway {

    /**
     * 新增数据集
     */
    Map<String, Object> addData(Map<String, Object> data);

    /**
     * 编辑数据集
     */
    Map<String, Object> editorData(Map<String, Object> data);

    /**
     * 删除数据集
     */
    boolean delData(String id);

    /**
     * 分页查询数据集
     */
    Map<String, Object> getData(String text, int pageNo, int pageSize);

    /**
     * 获取版本数据
     */
    Map<String, Object> getVerData(String dictVerId);

    /**
     * 编辑版本数据
     */
    Map<String, Object> editorVerData(Map<String, Object> d, String dictVerId);

    /**
     * 删除字典版本数据
     */
    boolean delDictVerData(String id);

    /**
     * 获取所有已发布的数据集
     */
    List<Map<String, Object>> allPublishedDsSet();

    /**
     * 根据数据集编码列表获取数据元列表
     */
    List<Map<String, Object>> allDsList(List<String> setCode);

    /**
     * 获取引用数据（单个编码）
     */
    List<Map<String, Object>> refData(String code);

    /**
     * 获取引用数据（多个编码）
     */
    Map<String, List<Map<String, Object>>> refData(Set<String> code);
}
