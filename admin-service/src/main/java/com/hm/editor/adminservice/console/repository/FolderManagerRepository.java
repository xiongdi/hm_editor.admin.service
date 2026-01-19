package com.hm.editor.adminservice.console.repository;

import com.hm.editor.adminservice.console.domain.EmrBaseFolder;
import com.hm.editor.common.utils.StringUtils;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * @author:wanglei
 * @date:2020/10/15
 * @desc:
 */
@Repository
public class FolderManagerRepository {

    private static final String FOLDER_NAME = "emrBaseFolder";

    @Autowired
    MongoTemplate template;

    public List<EmrBaseFolder> findAll(String folderName) {
        Query q = new Query();
        if (StringUtils.isNotBlank(folderName)) {
            q.addCriteria(Criteria.where("name").regex(folderName));
        }
        return template.find(q.with(Sort.by(Sort.Direction.ASC, "order")), EmrBaseFolder.class);
    }

    public boolean exitor(List<EmrBaseFolder> folders) {
        BulkOperations ops = template.bulkOps(
            BulkOperations.BulkMode.UNORDERED,
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

    /** 根据ID查询目录 */
    public EmrBaseFolder findById(String id) {
        Query q = Query.query(Criteria.where("_id").is(new ObjectId(id)));
        return template.findOne(q, EmrBaseFolder.class);
    }

    /** 保存目录（新增或更新） */
    public boolean save(EmrBaseFolder folder) {
        try {
            template.save(folder);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** 根据ID删除目录 */
    public boolean deleteById(String id) {
        Query q = Query.query(Criteria.where("_id").is(new ObjectId(id)));
        return template.remove(q, EmrBaseFolder.class).getDeletedCount() > 0;
    }
}
