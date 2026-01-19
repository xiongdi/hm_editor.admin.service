package com.hm.editor.adminservice.console.service;

import com.hm.editor.adminservice.console.domain.EmrBaseFolder;
import com.hm.editor.adminservice.console.repository.FolderManagerRepository;
import java.util.Date;
import java.util.List;
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
  @Autowired FolderManagerRepository folderManagerRepository;

  public List<EmrBaseFolder> findAll(String folderName) {
    List<EmrBaseFolder> baseFolders = folderManagerRepository.findAll(folderName);
    baseFolders.forEach(b -> b.initIDStr());
    return baseFolders;
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
