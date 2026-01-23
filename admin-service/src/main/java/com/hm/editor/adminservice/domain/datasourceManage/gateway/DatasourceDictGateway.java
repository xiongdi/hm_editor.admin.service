package com.hm.editor.adminservice.domain.datasourceManage.gateway;

import java.util.List;
import java.util.Map;

/**
 * 数据源字典Gateway接口
 */
public interface DatasourceDictGateway {

    /**
     * 新增字典
     */
    boolean addDSDict(Map<String, Object> data);

    /**
     * 编辑字典
     */
    boolean editorDSDict(Map<String, Object> data);

    /**
     * 删除字典
     */
    boolean delDSDict(String id);

    /**
     * 分页查询字典
     */
    Map<String, Object> getDict(String text, int pageNo, int pageSize);

    /**
     * 获取所有字典
     */
    List<Map<String, Object>> allDict();

    /**
     * 获取所有已使用的字典
     */
    List<Map<String, Object>> allUsedDict();

    /**
     * 获取字典版本数据
     */
    List<Map<String, Object>> getDictVerData(String dictVerId);

    /**
     * 编辑字典版本数据
     */
    List<Map<String, Object>> editorDsDictVerData(Map<String, Object> d, String dictVerId);

    /**
     * 删除字典版本数据
     */
    boolean delDsDictVerData(String id);

    /**
     * 根据编码获取字典版本数据
     */
    List<Map<String, Object>> getDsDictVerDataByCode(String code);

    /**
     * 获取引用数据
     */
    List<Map<String, Object>> refData(String code);
}
