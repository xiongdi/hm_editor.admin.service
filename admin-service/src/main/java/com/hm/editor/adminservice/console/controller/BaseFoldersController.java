package com.hm.editor.adminservice.console.controller;

import com.hm.editor.common.dto.ApiResult;
import com.hm.editor.adminservice.console.domain.EmrDataElementAdmin;
import com.hm.editor.adminservice.console.domain.EmrDataSet;
import com.hm.editor.adminservice.console.service.BaseFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/BaseFolders")
public class BaseFoldersController {
    @Autowired
    private BaseFolderService baseFolderService;

    @RequestMapping("/getAll")
    public ApiResult getAll(){
        List<EmrDataSet> list = null;
        list = baseFolderService.getAll();
        return ApiResult.success("查询成功", list);
    }

    @RequestMapping("/getCondition")
    public ApiResult getCondition(String id){
        List<EmrDataElementAdmin> list = null;
        list = baseFolderService.getCondition(id);
        return ApiResult.success("查询成功", list);
    }

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

    @RequestMapping("/delBaseFolder")
    public ApiResult delBaseFolder(@RequestParam String id) {
        boolean flag = baseFolderService.delBaseFolder(id);
        return ApiResult.success();
    }

    @RequestMapping("/editBaseFolder")
    public ApiResult editBaseFolder(@RequestBody Map<String,Object> param) {
        boolean flag = baseFolderService.editBaseFolder(param);
        return flag?ApiResult.success():ApiResult.failed();
    }

}
