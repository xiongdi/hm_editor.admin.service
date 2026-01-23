package com.hm.editor.adminservice.domain.console.gateway;

import com.hm.editor.adminservice.domain.console.model.EmrDataElementAdmin;
import com.hm.editor.adminservice.domain.console.model.EmrDataSet;
import com.hm.editor.adminservice.domain.console.model.EmrBaseFolder;
import com.hm.editor.adminservice.domain.console.model.EmrBaseTemplate;
import java.util.List;
import java.util.Map;

/**
 * 基础目录Gateway接口
 */
public interface BaseFolderGateway {

    /**
     * 获取所有数据集
     */
    List<EmrDataSet> getAll();

    /**
     * 根据条件查询数据元
     */
    List<EmrDataElementAdmin> getCondition(String id);

    /**
     * 获取数据集
     */
    Map<String, Object> getDataSet(Map<String, Object> param);

    /**
     * 删除基础目录
     */
    boolean deleteBaseFolder(String id);

    /**
     * 新增基础目录
     */
    boolean addBaseFolder(Map<String, Object> emrBaseFolder);

    /**
     * 编辑基础目录
     */
    boolean editBaseFolder(Map<String, Object> emrBaseFolder);

    /**
     * 根据名称查询目录
     */
    EmrBaseFolder getFolderByName(String name);

    /**
     * 根据名称查询模板
     */
    EmrBaseTemplate getTemplateByName(String name);

    /**
     * 获取下一个目录索引
     */
    int nextFolderIndex();

    /**
     * 保存对象
     */
    void save(Object entity);
}
