package com.hm.editor.adminservice.datasourceManage.controller;

import com.hm.editor.common.dto.ApiResult;
import com.hm.editor.adminservice.datasourceManage.service.DatasourceDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "数据源字典管理")
@RestController
@RequestMapping("/datasource")
public class DatasourceDictController {
    @Autowired
    DatasourceDictService datasourceDictService;

    @ApiOperation("编辑字典")
    @RequestMapping("/dict/editorDict")
    public ApiResult editorDict(@RequestBody Map d){
        return ApiResult.success(datasourceDictService.editorDSDict(d));
    }

    @ApiOperation("获取字典")
    @RequestMapping("/dict/getDict")
    public ApiResult getDict(@ApiParam("搜索文本") @RequestParam("text")String text,@ApiParam("页码") @RequestParam("pageNo")int pageNo,@ApiParam("每页大小") @RequestParam("pageSize")int pageSize){
        return ApiResult.success(datasourceDictService.getDict(text,pageNo,pageSize));
    }

    @ApiOperation("删除字典")
    @RequestMapping("/dict/delDict")
    public ApiResult delDict(@ApiParam("字典ID") @RequestParam("id")String id){
        return ApiResult.success(datasourceDictService.delDSDict(id));
    }

    @ApiOperation("导出字典")
    @RequestMapping("/dict/exportDict")
    public void exportDict(@ApiParam("搜索文本") @RequestParam("text")String text,@ApiParam("页码") @RequestParam("pageNo")int pageNo,@ApiParam("每页大小") @RequestParam("pageSize")int pageSize){
        // todo
        //return ApiResult.success(datasourceDictService.getDict(text,pageNo,pageSize));
    }

    @ApiOperation("获取所有字典")
    @RequestMapping("/dict/allDict")
    public ApiResult allDict(){
        return ApiResult.success(datasourceDictService.allDict());
    }

    // 启用且已发布值域
    @ApiOperation("获取已启用的字典")
    @RequestMapping("/dict/allUsedDict")
    public ApiResult allUsedDict(){
        return ApiResult.success(datasourceDictService.allUsedDict());
    }

    @ApiOperation("获取字典版本数据")
    @RequestMapping("/dict/getDictVerData")
    public ApiResult getDictVerData(@ApiParam("字典版本ID") @RequestParam("dictVerId")String dictVerId){
        return ApiResult.success(datasourceDictService.getDictVerData(dictVerId));
    }

    @ApiOperation("编辑字典版本数据")
    @RequestMapping("/dict/editorDictVerData")
    public ApiResult editorDictVerData(@RequestBody Map<String,Object> data,@ApiParam("字典版本ID") @RequestParam("dictVerId")String dictVerId){
        return ApiResult.success(datasourceDictService.editorDsDictVerData(data,dictVerId));
    }

    @ApiOperation("删除字典版本数据")
    @RequestMapping("/dict/delDictVerData")
    public ApiResult delDictVerData(@ApiParam("ID") @RequestParam("id") String id){
        return ApiResult.success(datasourceDictService.delDsDictVerData(id));
    }

    @ApiOperation("通过代码获取字典版本数据")
    @RequestMapping("/dict/getDictVerDataByCode")
    public ApiResult getDictVerDataByCode(@ApiParam("代码") @RequestParam("code") String code){
        return ApiResult.success(datasourceDictService.getDsDictVerDataByCode(code));
    }

    @ApiOperation("引用数据")
    @RequestMapping("/dict/refData")
    public ApiResult refData(@ApiParam("代码") @RequestParam("code") String code){
        return ApiResult.success(datasourceDictService.refData(code));
    }

}
