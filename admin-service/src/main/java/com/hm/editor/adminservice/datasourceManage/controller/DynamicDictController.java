package com.hm.editor.adminservice.datasourceManage.controller;

import com.hm.editor.adminservice.datasourceManage.service.DynamicDictService;
import com.hm.editor.common.dto.ApiResult;
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
  @Autowired DynamicDictService dynamicDictService;

  @Operation(summary = "编辑动态值域")
  @RequestMapping(value = "/dynamicDict/editorDynamicDict", method = RequestMethod.POST)
  public ApiResult editorDynamicDict(@RequestBody Map d) {
    try {
      return ApiResult.success(dynamicDictService.editorDynamicDict(d));
    } catch (Throwable e) {
      return ApiResult.failed(e.getMessage());
    }
  }

  @Operation(summary = "获取动态值域")
  @RequestMapping(value = "/dynamicDict/getDynamicDict", method = RequestMethod.GET)
  public ApiResult getDynamicDict(
      @Parameter(description = "搜索文本") @RequestParam("text") String text,
      @Parameter(description = "页码") @RequestParam("pageNo") int pageNo,
      @Parameter(description = "每页大小") @RequestParam("pageSize") int pageSize) {
    return ApiResult.success(dynamicDictService.getDynamicDict(text, pageNo, pageSize));
  }

  /** 逻辑删除动态值域（设置isDeleted=1） 只是标记删除状态，不会物理删除数据 */
  @Operation(summary = "删除动态值域")
  @RequestMapping(value = "/dynamicDict/delDynamicDict", method = RequestMethod.GET)
  public ApiResult delDynamicDict(
      @Parameter(description = "动态值域ID") @RequestParam("id") String id) {
    return ApiResult.success(dynamicDictService.delDynamicDict(id));
  }

  @Operation(summary = "获取所有动态值域")
  @RequestMapping(value = "/dynamicDict/allDynamicDict", method = RequestMethod.GET)
  public ApiResult allDynamicDict() {
    return ApiResult.success(dynamicDictService.allDynamicDict());
  }

  @Operation(summary = "通过代码获取动态值域")
  @RequestMapping(value = "/dynamicDict/getDynamicDictByCode", method = RequestMethod.GET)
  public ApiResult getDynamicDictByCode(
      @Parameter(description = "代码") @RequestParam("code") String code) {
    return ApiResult.success(dynamicDictService.getDynamicDictByCode(code));
  }
}
