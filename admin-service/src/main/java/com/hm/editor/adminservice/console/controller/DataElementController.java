package com.hm.editor.adminservice.console.controller;

import com.hm.editor.adminservice.console.service.DataElementService;
import com.hm.editor.common.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "数据元管理", description = "数据元管理相关接口")
@RestController
@RequestMapping("/dataElement")
public class DataElementController {

    @Autowired
    private DataElementService dataElementService;

    @Operation(summary = "搜索数据元")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ApiResult<Object> getAll(@RequestBody Map<String, Object> param) {
        return ApiResult.success("查询成功", dataElementService.getAll(param));
    }

    @Operation(summary = "删除数据元")
    @RequestMapping(value = "/delDataElenment", method = RequestMethod.POST)
    public ApiResult<Object> delDataElenment(
        @Parameter(description = "数据元ID") @RequestParam String id
    ) {
        boolean flag = dataElementService.delDataElenment(id);
        return flag ? ApiResult.success("删除成功") : ApiResult.failed("删除失败");
    }

    @Operation(summary = "修改数据元")
    @RequestMapping(value = "/updDataElenment", method = RequestMethod.POST)
    public ApiResult<Object> updDataElenment(@RequestBody Map<String, Object> param) {
        boolean flag = dataElementService.updDataElenment(param);
        return flag ? ApiResult.success("修改成功") : ApiResult.failed("修改失败");
    }

    @Operation(summary = "新建数据元")
    @RequestMapping(value = "/editDataElement", method = RequestMethod.POST)
    public ApiResult<Object> intDataElenment(@RequestBody Map<String, Object> param) {
        boolean flag = dataElementService.intDataElenment(param);
        return flag
            ? ApiResult.success("新建数据元成功!")
            : ApiResult.failed("已经存在相同名称、相同类型的数据元，无法新增");
    }

    @Operation(summary = "导出数据元")
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void export(HttpServletResponse response) {
        dataElementService.export(response);
    }

    @Operation(summary = "上传数据元文件")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public boolean upload(
        @Parameter(description = "上传文件") @RequestParam("uploadfile") MultipartFile file
    ) {
        return dataElementService.upload(file);
    }

    // 保存模板绑定数据元
    @Operation(summary = "模板自动绑定数据元")
    @RequestMapping(value = "/autoBind", method = RequestMethod.POST)
    public ApiResult<Object> autoBind(@RequestBody Map<String, Object> templateDatasource) {
        boolean flag = dataElementService.autoBind(templateDatasource);
        if (flag) {
            return ApiResult.success();
        }
        return ApiResult.failed("");
    }
}
