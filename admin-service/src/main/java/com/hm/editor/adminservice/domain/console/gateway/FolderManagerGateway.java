package com.hm.editor.adminservice.domain.console.gateway;

import com.hm.editor.adminservice.domain.console.model.EmrBaseFolder;
import java.util.List;

/**
 * 目录管理Gateway接口
 */
public interface FolderManagerGateway {

    /**
     * 查询所有目录
     */
    List<EmrBaseFolder> findAll(String folderName);

    /**
     * 统计总数
     */
    long count(String folderName);

    /**
     * 分页查询
     */
    List<EmrBaseFolder> findPage(String folderName, int pageNo, int pageSize);

    /**
     * 批量更新
     */
    boolean batchUpdate(List<EmrBaseFolder> folders);

    /**
     * 根据ID查询
     */
    EmrBaseFolder findById(String id);

    /**
     * 保存目录（新增或更新）
     */
    boolean save(EmrBaseFolder folder);

    /**
     * 根据ID删除
     */
    boolean deleteById(String id);
}
