package com.hm.editor.adminservice.domain.console.model;

import java.io.Serializable;
import java.util.Date;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author:wanglei
 * @date:2020/10/15
 * @desc:基础目录实体
 */
@Document(collection = "emrBaseFolder")
public class EmrBaseFolder implements Serializable {

    private static final long serialVersionUID = 89843781789L;
    
    @Id
    private ObjectId _id;
    private String name;
    private int order;
    private String idStr;
    private Date createTime;
    private String status;
    private String remark;

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "{name:" + name + "}";
    }

    public void initIDStr() {
        if (_id == null) {
            idStr = "";
        } else {
            idStr = _id.toHexString();
        }
    }
}
