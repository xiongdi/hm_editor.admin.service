package com.hm.editor.adminservice.datasourceManage.controller;

import com.hm.editor.common.dto.ApiResult;
import com.hm.editor.adminservice.datasourceManage.service.DatasourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "数据源管理")
@RestController
@RequestMapping("/datasource")
public class DatasourceController {
    @Autowired
    DatasourceService datasourceService;

    @ApiOperation("获取数据源")
    @RequestMapping("/ds/getDs")
    public ApiResult getDs(@ApiParam("搜索文本") @RequestParam("text")String text,@ApiParam("页码") @RequestParam("pageNo")int pageNo,@ApiParam("每页大小") @RequestParam("pageSize")int pageSize){
        return ApiResult.success(datasourceService.getDs(text,pageNo,pageSize));
    }

    @ApiOperation("编辑数据源")
    @RequestMapping("/ds/editorDs")
    public ApiResult editoDs(@RequestBody Map d){
        return ApiResult.success(datasourceService.editorDS(d));
    }

    @ApiOperation("删除数据源")
    @RequestMapping("/ds/delDs")
    public ApiResult delDs(@ApiParam("数据源ID") @RequestParam("id")String id){
        return ApiResult.success(datasourceService.delDs(id));
    }

    @ApiOperation("获取所有数据源")
    @RequestMapping("/ds/getAllDs")
    public ApiResult getAllDs(){
        return ApiResult.success(datasourceService.getAllDs());
    }

    @ApiOperation("引用数据")
    @RequestMapping("/ds/refData")
    public ApiResult refData(@ApiParam("代码") @RequestParam("code") String code){
        return ApiResult.success(datasourceService.refData(code));
    }

    @ApiOperation("生成代码")
    @RequestMapping("/ds/generalCode")
    public ApiResult generalCode(@RequestBody Map<String,String> param){
        return ApiResult.success(datasourceService.generalCode(param.get("name")));
    }

}
