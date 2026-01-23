package com.hm.editor.adminservice.infrastructure.persistence.console;

import com.hm.editor.adminservice.domain.console.model.EmrBaseFolder;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 目录管理Repository接口
 */
@Repository
public interface FolderManagerRepository extends MongoRepository<EmrBaseFolder, org.bson.types.ObjectId> {

    /**
     * 根据名称模糊查询（忽略大小写）
     */
    List<EmrBaseFolder> findByNameContainingIgnoreCaseOrderByOrderAsc(String name);

    /**
     * 根据名称统计
     */
    long countByNameContainingIgnoreCase(String name);

    /**
     * 分页查询
     */
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    Page<EmrBaseFolder> findByNameRegex(String name, Pageable pageable);
}
