package com.hm.editor.adminservice.console.repository;

import com.hm.editor.common.utils.StringUtils;
import com.hm.editor.adminservice.console.domain.EmrBaseFolder;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author:wanglei
 * @date:2020/10/15
 * @desc:
 */
@Repository
public class FolderManagerRepository {
    private String FOLDER_NAME = "emrBaseFolder";
    @Autowired
    MongoTemplate template;
    public List<EmrBaseFolder> findAll(String folderName){
        Query q = new Query();
        if(StringUtils.isNotBlank(folderName)){
            q.addCriteria(Criteria.where("name").regex(folderName));
        }
        return template.find(q.with(Sort.by(Sort.Direction.ASC,"order")),EmrBaseFolder.class);
    }
    public boolean exitor(List<EmrBaseFolder> folders){
        BulkOperations ops = template.bulkOps(BulkOperations.BulkMode.UNORDERED,EmrBaseFolder.class);
        for(EmrBaseFolder f:folders){
           Update u = new Update();
           u.set("order",f.getOrder());
           Query q = Query.query(Criteria.where("_id").is(new ObjectId(f.getIdStr())));
           ops.updateOne(q,u);
        }
        return ops.execute().getModifiedCount() > 0;
    }
}
