package com.hm.editor.adminservice.console.controller;

import com.hm.editor.common.dto.ApiResult;
import com.hm.editor.adminservice.console.domain.EmrBaseFolder;
import com.hm.editor.adminservice.console.service.FolderManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author:wanglei
 * @date:2020/10/15
 * @desc:目录管理
 */
@RestController
@RequestMapping("/")
public class FolderManagerController {

    @Autowired
    FolderManagerService folderManagerService;

    @RequestMapping("/baseFolders")
    public ApiResult getAll(@RequestParam("folderName") String folderName){
        return ApiResult.success(folderManagerService.findAll(folderName));
    }
    @RequestMapping("/moveFolder")
    public ApiResult moveFolder(@RequestBody List<EmrBaseFolder> param){
        return ApiResult.success(folderManagerService.editor(param));
    }

}
