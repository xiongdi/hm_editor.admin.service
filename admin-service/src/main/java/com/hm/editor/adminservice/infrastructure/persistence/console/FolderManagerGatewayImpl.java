package com.hm.editor.adminservice.infrastructure.persistence.console;

import com.hm.editor.adminservice.domain.console.gateway.FolderManagerGateway;
import com.hm.editor.adminservice.domain.console.model.EmrBaseFolder;
import com.hm.editor.adminservice.infrastructure.persistence.console.FolderManagerRepository;
import com.hm.editor.adminservice.infrastructure.utils.StringUtils;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * 目录管理Gateway实现
 */
@Component
public class FolderManagerGatewayImpl implements FolderManagerGateway {

    @Autowired
    private FolderManagerRepository folderManagerRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<EmrBaseFolder> findAll(String folderName) {
        if (StringUtils.isNotBlank(folderName)) {
            return folderManagerRepository.findByNameContainingIgnoreCaseOrderByOrderAsc(
                folderName
            );
        }
        return folderManagerRepository.findAll(Sort.by(Sort.Direction.ASC, "order"));
    }

    @Override
    public long count(String folderName) {
        if (StringUtils.isNotBlank(folderName)) {
            return folderManagerRepository.countByNameContainingIgnoreCase(folderName);
        }
        return folderManagerRepository.count();
    }

    @Override
    public List<EmrBaseFolder> findPage(String folderName, int pageNo, int pageSize) {
        int safePageNo = Math.max(pageNo, 1);
        int safePageSize = Math.max(pageSize, 1);
        PageRequest pageRequest = PageRequest.of(
            safePageNo - 1,
            safePageSize,
            Sort.by(Sort.Direction.ASC, "order")
        );

        if (StringUtils.isNotBlank(folderName)) {
            Page<EmrBaseFolder> page = folderManagerRepository.findByNameRegex(
                folderName,
                pageRequest
            );
            return page.getContent();
        }
        Page<EmrBaseFolder> page = folderManagerRepository.findAll(pageRequest);
        return page.getContent();
    }

    @Override
    public boolean batchUpdate(List<EmrBaseFolder> folders) {
        org.springframework.data.mongodb.core.BulkOperations ops = mongoTemplate.bulkOps(
            org.springframework.data.mongodb.core.BulkOperations.BulkMode.UNORDERED,
            EmrBaseFolder.class
        );
        for (EmrBaseFolder f : folders) {
            Update u = new Update();
            u.set("order", f.getOrder());
            Query q = Query.query(Criteria.where("_id").is(new ObjectId(f.getIdStr())));
            ops.updateOne(q, u);
        }
        return ops.execute().getModifiedCount() > 0;
    }

    @Override
    public EmrBaseFolder findById(String id) {
        if (id == null || !ObjectId.isValid(id)) {
            return null;
        }
        return folderManagerRepository.findById(new ObjectId(id)).orElse(null);
    }

    @Override
    public boolean save(EmrBaseFolder folder) {
        try {
            folderManagerRepository.save(folder);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteById(String id) {
        try {
            if (id == null || !ObjectId.isValid(id)) {
                return false;
            }
            folderManagerRepository.deleteById(new ObjectId(id));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
