package com.hm.editor.adminservice.console.controller;

import com.hm.editor.adminservice.console.domain.EmrBaseFolder;
import com.hm.editor.adminservice.console.domain.EmrBaseTemplate;
import com.hm.editor.adminservice.console.service.FolderTemplateService;
import com.hm.editor.common.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @PROJECT_NAME:service
 *
 * @author:wanglei
 * @date:2020/11/11 11:12 AM @Description:模板目录管理
 */
@Tag(name = "模板目录管理", description = "模板目录管理相关接口")
@RestController
@RequestMapping("/")
public class FolderTemplateController {

  @Autowired FolderTemplateService folderTemplateService;

  @Operation(summary = "获取所有基础模板")
  @RequestMapping(value = "/baseTemplates", method = RequestMethod.POST)
  public ApiResult getAll(@RequestBody Map<String, Object> param) {
    return ApiResult.success(folderTemplateService.getAll(param));
  }

  @Operation(summary = "删除基础模板")
  @RequestMapping(value = "/delBaseTemplate", method = RequestMethod.POST)
  public ApiResult del(@RequestBody Map<String, String> id) {
    boolean flag = folderTemplateService.delBasTemplate(id.get("id"));
    return flag ? ApiResult.success() : ApiResult.failed();
  }

  @Operation(summary = "保存基础模板HTML")
  @RequestMapping(value = "/saveBaseTemplateHtml", method = RequestMethod.POST)
  public ApiResult saveBaseTemplateHtml(@RequestBody Map<String, String> templateHtml) {
    boolean flag =
        folderTemplateService.saveBaseTemplateHtml(
            templateHtml.get("id"), templateHtml.get("html"));
    return flag ? ApiResult.success() : ApiResult.failed();
  }

  @Operation(summary = "获取基础模板HTML")
  @RequestMapping(value = "/getBaseTemplateHtml", method = RequestMethod.POST)
  public ApiResult getBaseTemplateHtml(@RequestBody Map<String, String> id) {
    String html = folderTemplateService.getBaseTemplateHtml(id.get("id"));
    return ApiResult.success(html);
  }

  @Operation(summary = "编辑基础模板")
  @RequestMapping(value = "/baseTemplate", method = RequestMethod.POST)
  public ApiResult edit(@RequestBody EmrBaseTemplate emrBaseFolder, @RequestParam String hosnum) {
    String msg = folderTemplateService.editorEmrFolder(emrBaseFolder, hosnum);
    return msg.length() > 0 ? ApiResult.failed(msg) : ApiResult.success();
  }

  @Operation(summary = "获取基础模板列表")
  @RequestMapping(value = "/getBasTemplates", method = RequestMethod.POST)
  public ApiResult getBasTemplates(@RequestBody EmrBaseFolder folder) {
    folder.set_id(new ObjectId(folder.getIdStr()));
    return folderTemplateService.getBasTemplateList(folder);
  }

  // 目录-模板对应关系
  @Operation(summary = "目录-模板对应关系")
  @RequestMapping(value = "/mapDirTemplate", method = RequestMethod.POST)
  public ApiResult mapDirTemplate() {
    return folderTemplateService.mapDirTemplate();
  }

  // 数据集对应所有数据元
  @Operation(summary = "模板数据集")
  @RequestMapping(value = "/templateDs", method = RequestMethod.GET)
  public ApiResult templateDs(@RequestParam String name) {
    return ApiResult.success(folderTemplateService.templateDs(name));
  }
}
