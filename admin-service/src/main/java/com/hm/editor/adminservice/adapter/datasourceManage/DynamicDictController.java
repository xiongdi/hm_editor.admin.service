package com.hm.editor.adminservice.adapter.datasourceManage;

import com.hm.editor.adminservice.app.datasourceManage.DynamicDictAppService;
import com.hm.editor.adminservice.infrastructure.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "动态值域管理", description = "动态值域管理相关接口")
@RestController
@RequestMapping("/datasource")
public class DynamicDictController {

    @Autowired
    DynamicDictAppService dynamicDictAppService;

    @Operation(summary = "新增动态值域")
    @RequestMapping(value = "/dynamicDict/addDynamicDict", method = RequestMethod.POST)
    public ApiResult<Object> addDynamicDict(@RequestBody Map<String, Object> d) {
        try {
            boolean flag = dynamicDictAppService.addDynamicDict(d);
            return flag ? ApiResult.success("新增成功") : ApiResult.failed("新增失败");
        } catch (Throwable e) {
            return ApiResult.failed(e.getMessage());
        }
    }

    @Operation(summary = "编辑动态值域")
    @RequestMapping(value = "/dynamicDict/editorDynamicDict", method = RequestMethod.POST)
    public ApiResult<Object> editorDynamicDict(@RequestBody Map<String, Object> d) {
        try {
            return ApiResult.success(dynamicDictAppService.editorDynamicDict(d));
        } catch (Throwable e) {
            return ApiResult.failed(e.getMessage());
        }
    }

    @Operation(summary = "获取动态值域")
    @RequestMapping(value = "/dynamicDict/getDynamicDict", method = RequestMethod.GET)
    public ApiResult<Object> getDynamicDict(
        @Parameter(description = "搜索文本") @RequestParam("text") String text,
        @Parameter(description = "页码") @RequestParam("pageNo") int pageNo,
        @Parameter(description = "每页大小") @RequestParam("pageSize") int pageSize
    ) {
        return ApiResult.success(dynamicDictAppService.getDynamicDict(text, pageNo, pageSize));
    }

    @Operation(summary = "删除动态值域")
    @RequestMapping(value = "/dynamicDict/delDynamicDict", method = RequestMethod.GET)
    public ApiResult<Object> delDynamicDict(
        @Parameter(description = "动态值域ID") @RequestParam("id") String id
    ) {
        return ApiResult.success(dynamicDictAppService.delDynamicDict(id));
    }

    @Operation(summary = "获取所有动态值域")
    @RequestMapping(value = "/dynamicDict/allDynamicDict", method = RequestMethod.GET)
    public ApiResult<Object> allDynamicDict() {
        return ApiResult.success(dynamicDictAppService.allDynamicDict());
    }

    @Operation(summary = "通过代码获取动态值域")
    @RequestMapping(value = "/dynamicDict/getDynamicDictByCode", method = RequestMethod.GET)
    public ApiResult<Object> getDynamicDictByCode(
        @Parameter(description = "代码") @RequestParam("code") String code
    ) {
        return ApiResult.success(dynamicDictAppService.getDynamicDictByCode(code));
    }
}
