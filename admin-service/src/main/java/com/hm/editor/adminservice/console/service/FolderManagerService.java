package com.hm.editor.adminservice.console.service;

import com.hm.editor.adminservice.console.domain.EmrBaseFolder;
import com.hm.editor.adminservice.console.repository.FolderManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:wanglei
 * @date:2020/10/15
 * @desc:
 */
@Service
public class FolderManagerService {
    @Autowired
    FolderManagerRepository folderManagerRepository;

    public List<EmrBaseFolder> findAll(String folderName){
        List<EmrBaseFolder> baseFolders = folderManagerRepository.findAll(folderName);
        baseFolders.forEach(b -> b.initIDStr());
        return baseFolders;
    }
    public boolean editor(List<EmrBaseFolder> folders){
        return folderManagerRepository.exitor(folders);
    }
}
