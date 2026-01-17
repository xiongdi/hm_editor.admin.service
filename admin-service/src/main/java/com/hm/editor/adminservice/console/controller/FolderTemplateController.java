package com.hm.editor.adminservice.console.controller;

import com.hm.editor.common.dto.ApiResult;
import com.hm.editor.adminservice.console.domain.EmrBaseFolder;
import com.hm.editor.adminservice.console.domain.EmrBaseTemplate;
import com.hm.editor.adminservice.console.service.FolderTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @PROJECT_NAME:service
 * @author:wanglei
 * @date:2020/11/11 11:12 AM
 * @Description:模板目录管理
 */
@Api(tags = "模板目录管理")
@RestController
@RequestMapping("/")
public class FolderTemplateController {

    @Autowired
    FolderTemplateService folderTemplateService;

    @ApiOperation("获取所有基础模板")
    @RequestMapping("/baseTemplates")
    public ApiResult getAll(@RequestBody Map<String,Object> param){
        return ApiResult.success(folderTemplateService.getAll(param));
    }

    @ApiOperation("删除基础模板")
    @RequestMapping(value = "/delBaseTemplate",method = RequestMethod.POST)
    public ApiResult del(@RequestBody Map<String,String> id){
        boolean flag = folderTemplateService.delBasTemplate(id.get("id"));
        return flag?ApiResult.success():ApiResult.failed();
    }

    @ApiOperation("保存基础模板HTML")
    @RequestMapping(value = "/saveBaseTemplateHtml",method = RequestMethod.POST)
    public ApiResult saveBaseTemplateHtml(@RequestBody Map<String,String> templateHtml){
        boolean flag = folderTemplateService.saveBaseTemplateHtml(templateHtml.get("id"),templateHtml.get("html"));
        return flag?ApiResult.success():ApiResult.failed();
    }

    @ApiOperation("获取基础模板HTML")
    @RequestMapping(value = "/getBaseTemplateHtml",method = RequestMethod.POST)
    public ApiResult getBaseTemplateHtml(@RequestBody Map<String,String> id){
        String html = folderTemplateService.getBaseTemplateHtml(id.get("id"));
        return ApiResult.success(html);
    }

    @ApiOperation("编辑基础模板")
    @RequestMapping(value = "/baseTemplate",method = RequestMethod.POST)
    public ApiResult edit(@RequestBody EmrBaseTemplate emrBaseFolder,@RequestParam String hosnum){
        String msg = folderTemplateService.editorEmrFolder(emrBaseFolder, hosnum);
        return msg.length() > 0?ApiResult.failed(msg):ApiResult.success();
    }

    @ApiOperation("获取基础模板列表")
    @RequestMapping(value = "/getBasTemplates",method = RequestMethod.POST)
    public ApiResult getBasTemplates(@RequestBody EmrBaseFolder folder){
        folder.set_id(new ObjectId(folder.getIdStr()));
        return folderTemplateService.getBasTemplateList(folder);
    }

    // 目录-模板对应关系
    @ApiOperation("目录-模板对应关系")
    @RequestMapping(value = "/mapDirTemplate",method = RequestMethod.POST)
    public ApiResult mapDirTemplate(){
        return folderTemplateService.mapDirTemplate();
    }

    // 数据集对应所有数据元
    @ApiOperation("模板数据集")
    @RequestMapping("/templateDs")
    public ApiResult templateDs(@RequestParam String name){
        return ApiResult.success(folderTemplateService.templateDs(name));
    }

}
