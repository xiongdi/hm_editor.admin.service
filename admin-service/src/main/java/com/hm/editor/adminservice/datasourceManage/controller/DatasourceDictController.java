package com.hm.editor.adminservice.datasourceManage.controller;

import com.hm.editor.adminservice.datasourceManage.service.DatasourceDictService;
import com.hm.editor.common.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "数据源字典管理", description = "数据源字典管理相关接口")
@RestController
@RequestMapping("/datasource")
public class DatasourceDictController {

    @Autowired
    DatasourceDictService datasourceDictService;

    @Operation(summary = "新增字典")
    @RequestMapping(value = "/dict/addDict", method = RequestMethod.POST)
    public ApiResult<Object> addDict(@RequestBody Map<String, Object> d) {
        boolean flag = datasourceDictService.addDSDict(d);
        return flag ? ApiResult.success("新增成功") : ApiResult.failed("新增失败");
    }

    @Operation(summary = "编辑字典")
    @RequestMapping(value = "/dict/editorDict", method = RequestMethod.POST)
    public ApiResult<Object> editorDict(@RequestBody Map<String, Object> d) {
        return ApiResult.success(datasourceDictService.editorDSDict(d));
    }

    @Operation(summary = "获取字典")
    @RequestMapping(value = "/dict/getDict", method = RequestMethod.GET)
    public ApiResult<Object> getDict(
        @Parameter(description = "搜索文本") @RequestParam("text") String text,
        @Parameter(description = "页码") @RequestParam("pageNo") int pageNo,
        @Parameter(description = "每页大小") @RequestParam("pageSize") int pageSize
    ) {
        return ApiResult.success(datasourceDictService.getDict(text, pageNo, pageSize));
    }

    @Operation(summary = "删除字典")
    @RequestMapping(value = "/dict/delDict", method = RequestMethod.GET)
    public ApiResult<Object> delDict(
        @Parameter(description = "字典ID") @RequestParam("id") String id
    ) {
        return ApiResult.success(datasourceDictService.delDSDict(id));
    }

    @Operation(summary = "导出字典")
    @RequestMapping(value = "/dict/exportDict", method = RequestMethod.GET)
    public ApiResult<Object> exportDict(
        @Parameter(description = "搜索文本") @RequestParam("text") String text,
        @Parameter(description = "页码") @RequestParam("pageNo") int pageNo,
        @Parameter(description = "每页大小") @RequestParam("pageSize") int pageSize
    ) {
        // TODO: 实现字典导出功能
        return ApiResult.success(datasourceDictService.getDict(text, pageNo, pageSize));
    }

    @Operation(summary = "获取所有字典")
    @RequestMapping(value = "/dict/allDict", method = RequestMethod.GET)
    public ApiResult<Object> allDict() {
        return ApiResult.success(datasourceDictService.allDict());
    }

    // 启用且已发布值域
    @Operation(summary = "获取已启用的字典")
    @RequestMapping(value = "/dict/allUsedDict", method = RequestMethod.GET)
    public ApiResult<Object> allUsedDict() {
        return ApiResult.success(datasourceDictService.allUsedDict());
    }

    @Operation(summary = "获取字典版本数据")
    @RequestMapping(value = "/dict/getDictVerData", method = RequestMethod.GET)
    public ApiResult<Object> getDictVerData(
        @Parameter(description = "字典版本ID") @RequestParam("dictVerId") String dictVerId
    ) {
        return ApiResult.success(datasourceDictService.getDictVerData(dictVerId));
    }

    @Operation(summary = "编辑字典版本数据")
    @RequestMapping(value = "/dict/editorDictVerData", method = RequestMethod.POST)
    public ApiResult<Object> editorDictVerData(
        @RequestBody Map<String, Object> data,
        @Parameter(description = "字典版本ID") @RequestParam("dictVerId") String dictVerId
    ) {
        return ApiResult.success(datasourceDictService.editorDsDictVerData(data, dictVerId));
    }

    @Operation(summary = "删除字典版本数据")
    @RequestMapping(value = "/dict/delDictVerData", method = RequestMethod.GET)
    public ApiResult<Object> delDictVerData(
        @Parameter(description = "ID") @RequestParam("id") String id
    ) {
        return ApiResult.success(datasourceDictService.delDsDictVerData(id));
    }

    @Operation(summary = "通过代码获取字典版本数据")
    @RequestMapping(value = "/dict/getDictVerDataByCode", method = RequestMethod.GET)
    public ApiResult<Object> getDictVerDataByCode(
        @Parameter(description = "代码") @RequestParam("code") String code
    ) {
        return ApiResult.success(datasourceDictService.getDsDictVerDataByCode(code));
    }

    @Operation(summary = "引用数据")
    @RequestMapping(value = "/dict/refData", method = RequestMethod.GET)
    public ApiResult<Object> refData(
        @Parameter(description = "代码") @RequestParam("code") String code
    ) {
        return ApiResult.success(datasourceDictService.refData(code));
    }
}
