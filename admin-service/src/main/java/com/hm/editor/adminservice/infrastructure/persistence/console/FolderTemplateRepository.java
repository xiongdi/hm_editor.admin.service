package com.hm.editor.adminservice.infrastructure.persistence.console;

import com.hm.editor.adminservice.domain.console.model.EmrBaseTemplate;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 模板管理Repository接口
 */
@Repository
public interface FolderTemplateRepository extends MongoRepository<EmrBaseTemplate, org.bson.types.ObjectId> {

    /**
     * 根据模板名称查询
     */
    List<EmrBaseTemplate> findByTemplateName(String templateName);

    /**
     * 根据目录ID查询
     */
    List<EmrBaseTemplate> findByFolderId(ObjectId folderId);

    /**
     * 根据目录ID和模板名称查询
     */
    @Query("{ 'folderId': ?0, 'templateName': { $regex: ?1 } }")
    List<EmrBaseTemplate> findByFolderIdAndTemplateNameRegex(ObjectId folderId, String templateName);

    /**
     * 检查是否存在同名模板（排除指定ID）
     */
    @Query("{ 'templateName': ?0, '_id': { $ne: ?1 } }")
    boolean existsByTemplateNameAndIdNot(String templateName, ObjectId id);

    /**
     * 统计总数（根据目录ID和模板名称）
     */
    @Query("{ 'folderId': ?0, 'templateName': { $regex: ?1 } }")
    long countByFolderIdAndTemplateNameRegex(ObjectId folderId, String templateName);
}
