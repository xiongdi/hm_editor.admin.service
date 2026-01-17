package com.hm.editor.adminservice.datasourceManage.controller;

import com.hm.editor.common.dto.ApiResult;
import com.hm.editor.adminservice.datasourceManage.service.DatasourceSetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "数据集管理")
@RestController
@RequestMapping("/datasource")
public class DatasourceSetController {
    @Autowired
    DatasourceSetService datasourceSetService;

    @ApiOperation("编辑数据集")
    @RequestMapping("/set/editorDsSet")
    public ApiResult editorSet(@RequestBody Map d){
        return ApiResult.success(datasourceSetService.editorData(d));
    }

    @ApiOperation("获取数据集")
    @RequestMapping("/set/getDsSet")
    public ApiResult getDict(@ApiParam("搜索文本") @RequestParam("text")String text,@ApiParam("页码") @RequestParam("pageNo")int pageNo,@ApiParam("每页大小") @RequestParam("pageSize")int pageSize){
        return ApiResult.success(datasourceSetService.getData(text,pageNo,pageSize));
    }

    @ApiOperation("删除数据集")
    @RequestMapping("/set/delDsSet")
    public ApiResult delDict(@ApiParam("数据集ID") @RequestParam("id")String id){
        return ApiResult.success(datasourceSetService.delData(id));
    }

    @ApiOperation("获取数据集版本数据")
    @RequestMapping("/set/getSetVerData")
    public ApiResult getSetVerData(@ApiParam("字典版本ID") @RequestParam("dictVerId")String dictVerId){
        return ApiResult.success(datasourceSetService.getVerData(dictVerId));
    }

    @ApiOperation("编辑数据集版本数据")
    @RequestMapping("/set/editorSetVerData")
    public ApiResult editorSetVerData(@RequestBody Map<String,Object> data,@ApiParam("字典版本ID") @RequestParam("dictVerId")String dictVerId){
        return ApiResult.success(datasourceSetService.editorVerData(data,dictVerId));
    }

    @ApiOperation("删除数据集版本数据")
    @RequestMapping("/set/delDsSetVerData")
    public ApiResult delDictVerData(@ApiParam("ID") @RequestParam("id") String id){
        return ApiResult.success(datasourceSetService.delDictVerData(id));
    }

    // 所有发布的数据集
    @ApiOperation("获取所有已发布的数据集")
    @RequestMapping("/set/allPublishedDsSet")
    public ApiResult allPublishedDsSet(){
        return ApiResult.success(datasourceSetService.allPublishedDsSet());
    }

    @ApiOperation("引用数据")
    @RequestMapping("/set/refData")
    public ApiResult refData(@ApiParam("代码") @RequestParam String code){
        return ApiResult.success(datasourceSetService.refData(code));
    }
}
