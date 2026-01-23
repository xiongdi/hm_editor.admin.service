package com.hm.editor.adminservice.domain.console.gateway;

import com.hm.editor.adminservice.domain.console.model.EmrBaseTemplate;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 * 模板管理Gateway接口
 */
public interface FolderTemplateGateway {

    /**
     * 获取总数
     */
    long getTotal(Map<String, Object> param);

    /**
     * 分页查询模板
     */
    List<EmrBaseTemplate> getAll(Map<String, Object> param);

    /**
     * 删除模板
     */
    boolean deleteById(String id);

    /**
     * 编辑模板
     */
    boolean update(EmrBaseTemplate template);

    /**
     * 检查是否存在同名模板
     */
    boolean existsByName(String id, String name);

    /**
     * 根据条件查询列表
     */
    List<EmrBaseTemplate> findByCriteria(Criteria criteria);

    /**
     * 根据目录ID查询
     */
    List<EmrBaseTemplate> findByFolderId(ObjectId folderId);

    /**
     * 获取所有模板（包含目录信息）
     */
    List<Map<String, Object>> findAllWithFolder();

    /**
     * 保存模板HTML
     */
    boolean saveTemplateHtml(String id, String html);

    /**
     * 获取模板HTML
     */
    String getTemplateHtml(String id);

    /**
     * 设置某个模板为其目录下默认模板，并取消原默认
     */
    boolean setDefaultTemplate(String templateId, boolean isDefault);
}
