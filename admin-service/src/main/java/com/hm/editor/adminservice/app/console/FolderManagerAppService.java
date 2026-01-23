package com.hm.editor.adminservice.app.console;

import com.hm.editor.adminservice.domain.console.gateway.FolderManagerGateway;
import com.hm.editor.adminservice.domain.console.model.EmrBaseFolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 目录管理应用服务
 */
@Service
public class FolderManagerAppService {

    @Autowired
    FolderManagerGateway folderManagerGateway;

    public List<EmrBaseFolder> findAll(String folderName) {
        List<EmrBaseFolder> baseFolders = folderManagerGateway.findAll(folderName);
        baseFolders.forEach(EmrBaseFolder::initIDStr);
        return baseFolders;
    }

    public Map<String, Object> findPage(String folderName, int pageNo, int pageSize) {
        long total = folderManagerGateway.count(folderName);
        List<EmrBaseFolder> data = total > 0
            ? folderManagerGateway.findPage(folderName, pageNo, pageSize)
            : new ArrayList<>();
        data.forEach(EmrBaseFolder::initIDStr);

        Map<String, Object> page = new HashMap<>();
        page.put("pageNo", pageNo);
        page.put("pageSize", pageSize);
        page.put("totalRecords", total);

        Map<String, Object> res = new HashMap<>();
        res.put("page", page);
        res.put("dataList", data);
        return res;
    }

    public boolean editor(List<EmrBaseFolder> folders) {
        return folderManagerGateway.batchUpdate(folders);
    }

    /** 根据ID查询目录 */
    public EmrBaseFolder findById(String id) {
        EmrBaseFolder folder = folderManagerGateway.findById(id);
        if (folder != null) {
            folder.initIDStr();
        }
        return folder;
    }

    /** 新增目录 */
    public boolean create(EmrBaseFolder folder) {
        if (folder.get_id() == null) {
            // 新增时生成ID
            folder.setCreateTime(new java.util.Date());
        }
        return folderManagerGateway.save(folder);
    }

    /** 更新目录（兼容旧接口：/updateFolder） */
    public boolean update(String id, EmrBaseFolder folder) {
        if (folder == null) {
            return false;
        }
        if (id != null && ObjectId.isValid(id)) {
            folder.set_id(new ObjectId(id));
        } else if (folder.get_id() == null) {
            // 兼容旧前端：可能只传了 idStr
            String idStr = folder.getIdStr();
            if (idStr != null && !idStr.isEmpty() && ObjectId.isValid(idStr)) {
                folder.set_id(new ObjectId(idStr));
            }
        }
        // 若前端直接传了 _id（ObjectId），这里也能正常 save
        return folderManagerGateway.save(folder);
    }

    /** 删除目录 */
    public boolean delete(String id) {
        return folderManagerGateway.deleteById(id);
    }
}
