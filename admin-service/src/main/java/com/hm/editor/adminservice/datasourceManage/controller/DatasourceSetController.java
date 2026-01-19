package com.hm.editor.adminservice.datasourceManage.controller;

import com.hm.editor.adminservice.datasourceManage.service.DatasourceSetService;
import com.hm.editor.common.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "数据集管理", description = "数据集管理相关接口")
@RestController
@RequestMapping("/datasource")
public class DatasourceSetController {

    @Autowired
    DatasourceSetService datasourceSetService;

    @Operation(summary = "新增数据集")
    @RequestMapping(value = "/set/addDsSet", method = RequestMethod.POST)
    public ApiResult<Object> addSet(@RequestBody Map<String, Object> d) {
        Map<String, Object> result = datasourceSetService.addData(d);
        boolean flag = (Boolean) result.getOrDefault("editFlag", false);
        return flag ? ApiResult.success("新增成功", result) : ApiResult.failed("新增失败");
    }

    @Operation(summary = "编辑数据集")
    @RequestMapping(value = "/set/editorDsSet", method = RequestMethod.POST)
    public ApiResult<Object> editorSet(@RequestBody Map<String, Object> d) {
        return ApiResult.success(datasourceSetService.editorData(d));
    }

    @Operation(summary = "获取数据集")
    @RequestMapping(value = "/set/getDsSet", method = RequestMethod.GET)
    public ApiResult<Object> getDict(
        @Parameter(description = "搜索文本") @RequestParam("text") String text,
        @Parameter(description = "页码") @RequestParam("pageNo") int pageNo,
        @Parameter(description = "每页大小") @RequestParam("pageSize") int pageSize
    ) {
        return ApiResult.success(datasourceSetService.getData(text, pageNo, pageSize));
    }

    @Operation(summary = "删除数据集")
    @RequestMapping(value = "/set/delDsSet", method = RequestMethod.GET)
    public ApiResult<Object> delDict(
        @Parameter(description = "数据集ID") @RequestParam("id") String id
    ) {
        return ApiResult.success(datasourceSetService.delData(id));
    }

    @Operation(summary = "获取数据集版本数据")
    @RequestMapping(value = "/set/getSetVerData", method = RequestMethod.GET)
    public ApiResult<Object> getSetVerData(
        @Parameter(description = "字典版本ID") @RequestParam("dictVerId") String dictVerId
    ) {
        return ApiResult.success(datasourceSetService.getVerData(dictVerId));
    }

    @Operation(summary = "编辑数据集版本数据")
    @RequestMapping(value = "/set/editorSetVerData", method = RequestMethod.POST)
    public ApiResult<Object> editorSetVerData(
        @RequestBody Map<String, Object> data,
        @Parameter(description = "字典版本ID") @RequestParam("dictVerId") String dictVerId
    ) {
        return ApiResult.success(datasourceSetService.editorVerData(data, dictVerId));
    }

    @Operation(summary = "删除数据集版本数据")
    @RequestMapping(value = "/set/delDsSetVerData", method = RequestMethod.GET)
    public ApiResult<Object> delDictVerData(
        @Parameter(description = "ID") @RequestParam("id") String id
    ) {
        return ApiResult.success(datasourceSetService.delDictVerData(id));
    }

    // 所有发布的数据集
    @Operation(summary = "获取所有已发布的数据集")
    @RequestMapping(value = "/set/allPublishedDsSet", method = RequestMethod.GET)
    public ApiResult<Object> allPublishedDsSet() {
        return ApiResult.success(datasourceSetService.allPublishedDsSet());
    }

    @Operation(summary = "引用数据")
    @RequestMapping(value = "/set/refData", method = RequestMethod.GET)
    public ApiResult<Object> refData(@Parameter(description = "代码") @RequestParam String code) {
        return ApiResult.success(datasourceSetService.refData(code));
    }
}
