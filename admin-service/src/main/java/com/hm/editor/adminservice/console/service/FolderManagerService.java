package com.hm.editor.adminservice.console.service;

import com.hm.editor.adminservice.console.domain.EmrBaseFolder;
import com.hm.editor.adminservice.console.repository.FolderManagerRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author:wanglei
 * @date:2020/10/15
 * @desc:
 */
@Service
public class FolderManagerService {

    @Autowired
    FolderManagerRepository folderManagerRepository;

    public List<EmrBaseFolder> findAll(String folderName) {
        List<EmrBaseFolder> baseFolders = folderManagerRepository.findAll(folderName);
        baseFolders.forEach(EmrBaseFolder::initIDStr);
        return baseFolders;
    }

    public Map<String, Object> findPage(String folderName, int pageNo, int pageSize) {
        long total = folderManagerRepository.count(folderName);
        List<EmrBaseFolder> data = total > 0
            ? folderManagerRepository.findPage(folderName, pageNo, pageSize)
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
        return folderManagerRepository.exitor(folders);
    }

    /** 根据ID查询目录 */
    public EmrBaseFolder findById(String id) {
        EmrBaseFolder folder = folderManagerRepository.findById(id);
        if (folder != null) {
            folder.initIDStr();
        }
        return folder;
    }

    /** 新增目录 */
    public boolean create(EmrBaseFolder folder) {
        if (folder.get_id() == null) {
            // 新增时生成ID
            folder.set_id(new ObjectId());
            folder.setCreateTime(new Date());
        }
        folder.initIDStr();
        return folderManagerRepository.save(folder);
    }

    /** 更新目录 */
    public boolean update(EmrBaseFolder folder) {
        if (folder.getIdStr() != null && !folder.getIdStr().isEmpty()) {
            folder.set_id(new ObjectId(folder.getIdStr()));
        }
        folder.initIDStr();
        return folderManagerRepository.save(folder);
    }

    /** 根据ID删除目录 */
    public boolean deleteById(String id) {
        return folderManagerRepository.deleteById(id);
    }
}
