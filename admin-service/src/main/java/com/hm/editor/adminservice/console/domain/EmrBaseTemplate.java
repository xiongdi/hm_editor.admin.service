package com.hm.editor.adminservice.console.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * @PROJECT_NAME:service_spce
 *
 * @author:wanglei
 * @date:2020/11/11 11:14 AM @Description:
 */
public class EmrBaseTemplate implements Serializable {

    private static final long serialVersionUID = 94736517838929274L;
    private ObjectId _id;
    private String templateName;
    private String isForbidMultiple;
    private String type;
    private EmrBaseFolder folder;
    private ObjectId folderId;
    private Date createDate;
    private Date editDate;
    private String folderStr;
    private String idStr;
    private List<String> dsSet;

    public String getIdStr() {
        return idStr;
    }

    public String getFolderStr() {
        return folderStr;
    }

    public void setFolderStr(String folderStr) {
        this.folderStr = folderStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getIsForbidMultiple() {
        return isForbidMultiple;
    }

    public void setIsForbidMultiple(String isForbidMultiple) {
        this.isForbidMultiple = isForbidMultiple;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EmrBaseFolder getFolder() {
        return folder;
    }

    public void setFolder(EmrBaseFolder folder) {
        this.folder = folder;
    }

    public ObjectId getFolderId() {
        return folderId;
    }

    public void setFolderId(ObjectId folderId) {
        this.folderId = folderId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    @Override
    public String toString() {
        return (
            "{templateName:" +
            templateName +
            ",isForbidMultiple:" +
            isForbidMultiple +
            ",type:" +
            type +
            ",folder:" +
            (folderId == null ? "" : folderId.toHexString()) +
            "}"
        );
    }

    public void initIDStr() {
        if (_id == null) {
            idStr = "";
        } else {
            idStr = _id.toHexString();
        }
        if (folderId == null) {
            folderStr = "";
        } else {
            folderStr = folderId.toHexString();
        }
        if (folder != null) {
            folder.initIDStr();
        }
    }

    public List<String> getDsSet() {
        return dsSet;
    }

    public void setDsSet(List<String> dsSet) {
        this.dsSet = dsSet;
    }
}
