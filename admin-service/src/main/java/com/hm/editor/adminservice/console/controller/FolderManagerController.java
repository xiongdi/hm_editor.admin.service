package com.hm.editor.adminservice.console.controller;

import com.hm.editor.adminservice.console.domain.EmrBaseFolder;
import com.hm.editor.adminservice.console.service.FolderManagerService;
import com.hm.editor.common.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author:wanglei
 * @date:2020/10/15
 * @desc:目录管理
 */
@Tag(name = "目录管理", description = "目录管理相关接口")
@RestController
@RequestMapping("/")
public class FolderManagerController {

    @Autowired
    FolderManagerService folderManagerService;

    @Operation(summary = "获取基础目录")
    @RequestMapping(value = "/baseFolders", method = RequestMethod.GET)
    public ApiResult<Object> getAll(
        @Parameter(description = "目录名称（模糊匹配）") @RequestParam(
            value = "folderName",
            required = false,
            defaultValue = ""
        ) String folderName
    ) {
        return ApiResult.success(folderManagerService.findAll(folderName));
    }

    @Operation(summary = "分页获取基础目录")
    @RequestMapping(value = "/baseFoldersPage", method = RequestMethod.GET)
    public ApiResult<Object> getPage(
        @Parameter(description = "目录名称（模糊匹配）") @RequestParam(
            value = "folderName",
            required = false,
            defaultValue = ""
        ) String folderName,
        @Parameter(description = "页码") @RequestParam("pageNo") int pageNo,
        @Parameter(description = "每页大小") @RequestParam("pageSize") int pageSize
    ) {
        return ApiResult.success(folderManagerService.findPage(folderName, pageNo, pageSize));
    }

    @Operation(summary = "移动目录")
    @RequestMapping(value = "/moveFolder", method = RequestMethod.POST)
    public ApiResult<Object> moveFolder(@RequestBody List<EmrBaseFolder> param) {
        return ApiResult.success(folderManagerService.editor(param));
    }

    @Operation(summary = "根据ID查询目录")
    @RequestMapping(value = "/getFolderById", method = RequestMethod.GET)
    public ApiResult<Object> getFolderById(
        @Parameter(description = "目录ID") @RequestParam String id
    ) {
        EmrBaseFolder folder = folderManagerService.findById(id);
        if (folder != null) {
            return ApiResult.success("查询成功", folder);
        } else {
            return ApiResult.failed("目录不存在");
        }
    }

    @Operation(summary = "新增目录")
    @RequestMapping(value = "/addFolder", method = RequestMethod.POST)
    public ApiResult<Object> addFolder(@RequestBody EmrBaseFolder folder) {
        boolean flag = folderManagerService.create(folder);
        return flag ? ApiResult.success("新增成功") : ApiResult.failed("新增失败");
    }

    @Operation(summary = "更新目录")
    @RequestMapping(value = "/updateFolder", method = RequestMethod.POST)
    public ApiResult<Object> updateFolder(@RequestBody EmrBaseFolder folder) {
        boolean flag = folderManagerService.update(folder);
        return flag ? ApiResult.success("更新成功") : ApiResult.failed("更新失败");
    }

    @Operation(summary = "删除目录")
    @RequestMapping(value = "/deleteFolder", method = RequestMethod.POST)
    public ApiResult<Object> deleteFolder(@RequestBody Map<String, String> param) {
        String id = param.get("id");
        boolean flag = folderManagerService.deleteById(id);
        return flag ? ApiResult.success("删除成功") : ApiResult.failed("删除失败");
    }
}
