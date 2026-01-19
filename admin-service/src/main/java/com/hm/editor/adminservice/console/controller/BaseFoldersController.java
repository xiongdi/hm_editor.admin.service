package com.hm.editor.adminservice.console.controller;

import com.hm.editor.adminservice.console.domain.EmrDataElementAdmin;
import com.hm.editor.adminservice.console.domain.EmrDataSet;
import com.hm.editor.adminservice.console.service.BaseFolderService;
import com.hm.editor.common.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "数据集管理", description = "数据集管理相关接口")
@RestController
@RequestMapping("/BaseFolders")
public class BaseFoldersController {
  @Autowired private BaseFolderService baseFolderService;

  @Operation(summary = "获取所有数据集")
  @RequestMapping(value = "/getAll", method = RequestMethod.GET)
  public ApiResult getAll() {
    List<EmrDataSet> list = null;
    list = baseFolderService.getAll();
    return ApiResult.success("查询成功", list);
  }

  @Operation(summary = "获取数据元条件")
  @RequestMapping(value = "/getCondition", method = RequestMethod.GET)
  public ApiResult getCondition(@Parameter(description = "数据集ID") @RequestParam String id) {
    List<EmrDataElementAdmin> list = null;
    list = baseFolderService.getCondition(id);
    return ApiResult.success("查询成功", list);
  }

  @Operation(summary = "获取数据集详情")
  @RequestMapping(value = "/getDataSet", method = RequestMethod.POST)
  public ApiResult getDataSet(@RequestBody Map<String, Object> param) {
    return ApiResult.success("查询成功", baseFolderService.getDataSet(param));
  }

  //    @RequestMapping("/findBaseFolder")
  //    public ApiResult findBaseFolder(@RequestBody EmrBaseFolder emrBaseFolder) {
  //        List<EmrBaseFolder> list = null;
  //        list = baseFolderService.findBaseFolder(emrBaseFolder);
  //        return ApiResult.success();
  //    }

  @Operation(summary = "删除数据集")
  @RequestMapping(value = "/delBaseFolder", method = RequestMethod.DELETE)
  public ApiResult delBaseFolder(@Parameter(description = "数据集ID") @RequestParam String id) {
    boolean flag = baseFolderService.delBaseFolder(id);
    return ApiResult.success();
  }

  @Operation(summary = "编辑数据集")
  @RequestMapping(value = "/editBaseFolder", method = RequestMethod.POST)
  public ApiResult editBaseFolder(@RequestBody Map<String, Object> param) {
    boolean flag = baseFolderService.editBaseFolder(param);
    return flag ? ApiResult.success() : ApiResult.failed();
  }
}
