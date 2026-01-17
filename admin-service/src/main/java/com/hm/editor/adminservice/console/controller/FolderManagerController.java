package com.hm.editor.adminservice.console.controller;

import com.hm.editor.common.dto.ApiResult;
import com.hm.editor.adminservice.console.domain.EmrBaseFolder;
import com.hm.editor.adminservice.console.service.FolderManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author:wanglei
 * @date:2020/10/15
 * @desc:目录管理
 */
@Api(tags = "目录管理")
@RestController
@RequestMapping("/")
public class FolderManagerController {

    @Autowired
    FolderManagerService folderManagerService;

    @ApiOperation("获取基础目录")
    @RequestMapping("/baseFolders")
    public ApiResult getAll(@RequestParam("folderName") String folderName){
        return ApiResult.success(folderManagerService.findAll(folderName));
    }

    @ApiOperation("移动目录")
    @RequestMapping("/moveFolder")
    public ApiResult moveFolder(@RequestBody List<EmrBaseFolder> param){
        return ApiResult.success(folderManagerService.editor(param));
    }

}
