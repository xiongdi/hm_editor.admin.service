package com.hm.editor.adminservice.console.controller;

import com.hm.editor.common.dto.ApiResult;
import com.hm.editor.adminservice.console.service.DataElementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Api(tags = "数据元管理")
@RestController
@RequestMapping("/dataElement")
public class DataElementController {

    @Autowired
    private DataElementService dataElementService;

    @ApiOperation("搜索数据元")
    @RequestMapping("/search")
    public ApiResult getAll(@RequestBody Map<String,Object> param) {
        return  ApiResult.success("查询成功", dataElementService.getAll(param));
    }

    @ApiOperation("删除数据元")
    @RequestMapping(value = "/delDataElenment", method = RequestMethod.POST)
    public  ApiResult delDataElenment(@ApiParam("数据元ID") @RequestParam String id) {
        boolean flag = dataElementService.delDataElenment(id);
        return flag?ApiResult.success("删除成功"):ApiResult.failed("删除失败");
    }

    @ApiOperation("修改数据元")
    @RequestMapping(value = "/updDataElenment", method = RequestMethod.POST)
    public ApiResult updDataElenment(@RequestBody Map<String,Object> param) {
        boolean flag = dataElementService.updDataElenment(param);
        return flag?ApiResult.success("修改成功"):ApiResult.failed("修改失败");
    }

    @ApiOperation("新建数据元")
    @RequestMapping(value = "/editDataElement", method = RequestMethod.POST)
    public ApiResult intDataElenment(@RequestBody Map<String,Object> param) {
        boolean flag = dataElementService.intDataElenment(param);
        return flag?ApiResult.success("新建数据元成功!"):ApiResult.failed("已经存在相同名称、相同类型的数据元，无法新增");
    }

    @ApiOperation("导出数据元")
    @RequestMapping(value = "/export")
    public void export(HttpServletResponse response){
        dataElementService.export(response);
    }

    @ApiOperation("上传数据元文件")
    @RequestMapping("/upload")
    public boolean upload(@ApiParam("上传文件") @RequestParam("uploadfile") MultipartFile file){
        return dataElementService.upload(file);
    }

    // 保存模板绑定数据元
    @ApiOperation("模板自动绑定数据元")
    @RequestMapping("/autoBind")
    public ApiResult autoBind(@RequestBody Map<String,Object> templateDatasource){
        boolean flag = dataElementService.autoBind(templateDatasource);
        if(flag){
            return ApiResult.success();
        }
        return ApiResult.failed("");
    }
 }
