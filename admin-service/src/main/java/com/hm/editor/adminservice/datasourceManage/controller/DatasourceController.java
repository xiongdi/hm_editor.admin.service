package com.hm.editor.adminservice.datasourceManage.controller;

import com.hm.editor.adminservice.datasourceManage.service.DatasourceService;
import com.hm.editor.common.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "数据源管理", description = "数据源管理相关接口")
@RestController
@RequestMapping("/datasource")
public class DatasourceController {

    @Autowired
    DatasourceService datasourceService;

    @Operation(summary = "获取数据源")
    @RequestMapping(value = "/ds/getDs", method = RequestMethod.GET)
    public ApiResult<Object> getDs(
        @Parameter(description = "搜索文本") @RequestParam("text") String text,
        @Parameter(description = "页码") @RequestParam("pageNo") int pageNo,
        @Parameter(description = "每页大小") @RequestParam("pageSize") int pageSize
    ) {
        return ApiResult.success(datasourceService.getDs(text, pageNo, pageSize));
    }

    @Operation(summary = "新增数据源")
    @RequestMapping(value = "/ds/addDs", method = RequestMethod.POST)
    public ApiResult<Object> addDs(@RequestBody Map<String, Object> d) {
        boolean flag = datasourceService.addDS(d);
        return flag ? ApiResult.success("新增成功") : ApiResult.failed("新增失败");
    }

    @Operation(summary = "编辑数据源")
    @RequestMapping(value = "/ds/editorDs", method = RequestMethod.POST)
    public ApiResult<Object> editoDs(@RequestBody Map<String, Object> d) {
        return ApiResult.success(datasourceService.editorDS(d));
    }

    @Operation(summary = "删除数据源")
    @RequestMapping(value = "/ds/delDs", method = RequestMethod.GET)
    public ApiResult<Object> delDs(
        @Parameter(description = "数据源ID") @RequestParam("id") String id
    ) {
        return ApiResult.success(datasourceService.delDs(id));
    }

    @Operation(summary = "获取所有数据源")
    @RequestMapping(value = "/ds/getAllDs", method = RequestMethod.GET)
    public ApiResult<Object> getAllDs() {
        return ApiResult.success(datasourceService.getAllDs());
    }

    @Operation(summary = "引用数据")
    @RequestMapping(value = "/ds/refData", method = RequestMethod.GET)
    public ApiResult<Object> refData(
        @Parameter(description = "代码") @RequestParam("code") String code
    ) {
        return ApiResult.success(datasourceService.refData(code));
    }

    @Operation(summary = "生成代码")
    @RequestMapping(value = "/ds/generalCode", method = RequestMethod.POST)
    public ApiResult<Object> generalCode(@RequestBody Map<String, String> param) {
        return ApiResult.success(datasourceService.generalCode(param.get("name")));
    }
}
