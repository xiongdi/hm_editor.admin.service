package com.hm.editor.adminservice.datasourceManage.controller;

import com.hm.editor.common.dto.ApiResult;
import com.hm.editor.adminservice.datasourceManage.service.DatasourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/datasource")
public class DatasourceController {
    @Autowired
    DatasourceService datasourceService;
    @RequestMapping("/ds/getDs")
    public ApiResult getDs(@RequestParam("text")String text,@RequestParam("pageNo")int pageNo,@RequestParam("pageSize")int pageSize){
        return ApiResult.success(datasourceService.getDs(text,pageNo,pageSize));
    }
    @RequestMapping("/ds/editorDs")
    public ApiResult editoDs(@RequestBody Map d){
        return ApiResult.success(datasourceService.editorDS(d));
    }
    @RequestMapping("/ds/delDs")
    public ApiResult delDs(@RequestParam("id")String id){
        return ApiResult.success(datasourceService.delDs(id));
    }
    @RequestMapping("/ds/getAllDs")
    public ApiResult getAllDs(){
        return ApiResult.success(datasourceService.getAllDs());
    }

    @RequestMapping("/ds/refData")
    public ApiResult refData(@RequestParam("code") String code){
        return ApiResult.success(datasourceService.refData(code));
    }

    @RequestMapping("/ds/generalCode")
    public ApiResult generalCode(@RequestBody Map<String,String> param){
        return ApiResult.success(datasourceService.generalCode(param.get("name")));
    }

}
