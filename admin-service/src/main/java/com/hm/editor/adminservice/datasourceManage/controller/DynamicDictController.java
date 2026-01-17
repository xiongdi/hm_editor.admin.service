package com.hm.editor.adminservice.datasourceManage.controller;

import com.hm.editor.common.dto.ApiResult;
import com.hm.editor.adminservice.datasourceManage.service.DynamicDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "动态值域管理")
@RestController
@RequestMapping("/datasource")
public class DynamicDictController {
    @Autowired
    DynamicDictService dynamicDictService;

    @ApiOperation("编辑动态值域")
    @RequestMapping("/dynamicDict/editorDynamicDict")
    public ApiResult editorDynamicDict(@RequestBody Map d){
        try{
            return ApiResult.success(dynamicDictService.editorDynamicDict(d));
        }catch (Throwable e){
            return ApiResult.failed(e.getMessage());
        }
    }

    @ApiOperation("获取动态值域")
    @RequestMapping("/dynamicDict/getDynamicDict")
    public ApiResult getDynamicDict(@ApiParam("搜索文本") @RequestParam("text") String text,
                                          @ApiParam("页码") @RequestParam("pageNo") int pageNo,
                                          @ApiParam("每页大小") @RequestParam("pageSize") int pageSize){
        return ApiResult.success(dynamicDictService.getDynamicDict(text, pageNo, pageSize));
    }

    /**
     * 逻辑删除动态值域（设置isDeleted=1）
     * 只是标记删除状态，不会物理删除数据
     */
    @ApiOperation("删除动态值域")
    @RequestMapping("/dynamicDict/delDynamicDict")
    public ApiResult delDynamicDict(@ApiParam("动态值域ID") @RequestParam("id") String id){
        return ApiResult.success(dynamicDictService.delDynamicDict(id));
    }

    @ApiOperation("获取所有动态值域")
    @RequestMapping("/dynamicDict/allDynamicDict")
    public ApiResult allDynamicDict(){
        return ApiResult.success(dynamicDictService.allDynamicDict());
    }

    @ApiOperation("通过代码获取动态值域")
    @RequestMapping("/dynamicDict/getDynamicDictByCode")
    public ApiResult getDynamicDictByCode(@ApiParam("代码") @RequestParam("code") String code){
        return ApiResult.success(dynamicDictService.getDynamicDictByCode(code));
    }
}
