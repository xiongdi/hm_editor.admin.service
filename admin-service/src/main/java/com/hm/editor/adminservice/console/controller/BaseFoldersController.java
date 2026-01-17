package com.hm.editor.adminservice.console.controller;

import com.hm.editor.common.dto.ApiResult;
import com.hm.editor.adminservice.console.domain.EmrDataElementAdmin;
import com.hm.editor.adminservice.console.domain.EmrDataSet;
import com.hm.editor.adminservice.console.service.BaseFolderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "数据集管理")
@RestController
@RequestMapping("/BaseFolders")
public class BaseFoldersController {
    @Autowired
    private BaseFolderService baseFolderService;

    @ApiOperation("获取所有数据集")
    @RequestMapping("/getAll")
    public ApiResult getAll(){
        List<EmrDataSet> list = null;
        list = baseFolderService.getAll();
        return ApiResult.success("查询成功", list);
    }

    @ApiOperation("获取数据元条件")
    @RequestMapping("/getCondition")
    public ApiResult getCondition(@ApiParam("数据集ID") String id){
        List<EmrDataElementAdmin> list = null;
        list = baseFolderService.getCondition(id);
        return ApiResult.success("查询成功", list);
    }

    @ApiOperation("获取数据集详情")
    @RequestMapping("/getDataSet")
    public ApiResult getDataSet(@RequestBody Map<String,Object> param) {
        return  ApiResult.success("查询成功", baseFolderService.getDataSet(param));
    }

//    @RequestMapping("/findBaseFolder")
//    public ApiResult findBaseFolder(@RequestBody EmrBaseFolder emrBaseFolder) {
//        List<EmrBaseFolder> list = null;
//        list = baseFolderService.findBaseFolder(emrBaseFolder);
//        return ApiResult.success();
//    }

    @ApiOperation("删除数据集")
    @RequestMapping("/delBaseFolder")
    public ApiResult delBaseFolder(@ApiParam("数据集ID") @RequestParam String id) {
        boolean flag = baseFolderService.delBaseFolder(id);
        return ApiResult.success();
    }

    @ApiOperation("编辑数据集")
    @RequestMapping("/editBaseFolder")
    public ApiResult editBaseFolder(@RequestBody Map<String,Object> param) {
        boolean flag = baseFolderService.editBaseFolder(param);
        return flag?ApiResult.success():ApiResult.failed();
    }

}
