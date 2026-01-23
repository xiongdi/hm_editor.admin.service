package com.hm.editor.adminservice.adapter.console;

import com.hm.editor.adminservice.app.console.FolderTemplateAppService;
import com.hm.editor.adminservice.domain.console.model.EmrBaseFolder;
import com.hm.editor.adminservice.domain.console.model.EmrBaseTemplate;
import com.hm.editor.adminservice.infrastructure.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Autowired
    FolderTemplateAppService folderTemplateAppService;

    @Operation(summary = "获取所有基础模板")
    @RequestMapping(value = "/baseTemplates", method = RequestMethod.POST)
    public ApiResult<Object> getAll(@RequestBody Map<String, Object> param) {
        return ApiResult.success(folderTemplateAppService.getAll(param));
    }

    @Operation(summary = "删除基础模板")
    @RequestMapping(value = "/delBaseTemplate", method = RequestMethod.POST)
    public ApiResult<Object> del(@RequestBody Map<String, String> id) {
        boolean flag = folderTemplateAppService.delBasTemplate(id.get("id"));
        return flag ? ApiResult.success() : ApiResult.failed();
    }

    @Operation(summary = "保存基础模板HTML")
    @RequestMapping(value = "/saveBaseTemplateHtml", method = RequestMethod.POST)
    public ApiResult<Object> saveBaseTemplateHtml(@RequestBody Map<String, String> templateHtml) {
        boolean flag = folderTemplateAppService.saveBaseTemplateHtml(
            templateHtml.get("id"),
            templateHtml.get("html")
        );
        return flag ? ApiResult.success() : ApiResult.failed();
    }

    @Operation(summary = "获取基础模板HTML")
    @RequestMapping(value = "/getBaseTemplateHtml", method = RequestMethod.POST)
    public ApiResult<Object> getBaseTemplateHtml(@RequestBody Map<String, String> id) {
        String html = folderTemplateAppService.getBaseTemplateHtml(id.get("id"));
        return ApiResult.success(html);
    }

    @Operation(summary = "编辑基础模板")
    @RequestMapping(value = "/baseTemplate", method = RequestMethod.POST)
    public ApiResult<Object> edit(
        @RequestBody EmrBaseTemplate emrBaseFolder,
        @RequestParam String hosnum
    ) {
        String msg = folderTemplateAppService.editorEmrFolder(emrBaseFolder, hosnum);
        return !msg.isEmpty() ? ApiResult.failed(msg) : ApiResult.success();
    }

    @Operation(summary = "获取基础模板列表")
    @RequestMapping(value = "/getBasTemplates", method = RequestMethod.POST)
    public ApiResult<Object> getBasTemplates(@RequestBody EmrBaseFolder folder) {
        folder.set_id(new ObjectId(folder.getIdStr()));
        return folderTemplateAppService.getBasTemplateList(folder);
    }

    // 目录-模板对应关系
    @Operation(summary = "目录-模板对应关系")
    @RequestMapping(value = "/mapDirTemplate", method = RequestMethod.POST)
    public ApiResult<Object> mapDirTemplate() {
        return folderTemplateAppService.mapDirTemplate();
    }

    // 数据集对应所有数据元
    @Operation(summary = "模板数据集")
    @RequestMapping(value = "/templateDs", method = RequestMethod.GET)
    public ApiResult<Object> templateDs(@RequestParam String name) {
        return ApiResult.success(folderTemplateAppService.templateDs(name));
    }

    /**
     * 设置或取消目录默认模板
     *
     * @param param 请求参数
     *              <ul>
     *                <li>id: 模板ID（必填）</li>
     *                <li>isDefault: 是否设为默认（可选，默认为 "1"），"1" 或不传表示设置为默认，"0" 表示取消默认</li>
     *              </ul>
     */
    @Operation(
        summary = "设置或取消目录默认模板",
        description = "**请求参数：**\n" +
            "- id: 模板ID（必填）\n" +
            "- isDefault: 是否设为默认（可选，默认为 \"1\"），\"1\" 或不传表示设置为默认，\"0\" 表示取消默认"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "请求参数：id（必填，模板ID），isDefault（可选，\"1\"或不传表示设置为默认，\"0\"表示取消默认）",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(
                type = "object",
                requiredProperties = {"id"},
                example = "{\"id\":\"507f1f77bcf86cd799439011\",\"isDefault\":\"1\"}"
            )
        )
    )
    @RequestMapping(value = "/setDefaultTemplate", method = RequestMethod.POST)
    public ApiResult<Object> setDefaultTemplate(@RequestBody Map<String, String> param) {
        String id = param == null ? null : param.get("id");
        // isDefault: 1(设为默认)/0(取消默认); 不传默认按 1 处理
        String isDefaultStr = param == null ? null : param.get("isDefault");
        boolean isDefault = isDefaultStr == null || isDefaultStr.isEmpty() || "1".equals(isDefaultStr);
        boolean flag = folderTemplateAppService.setDefaultTemplate(id, isDefault);
        if (!flag) {
            return ApiResult.failed(isDefault ? "设置失败" : "取消失败");
        }
        return ApiResult.success(isDefault ? "设置成功" : "取消成功");
    }
}
