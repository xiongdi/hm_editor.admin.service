package com.hm.editor.adminservice.datasourceManage.controller;

import com.hm.editor.common.dto.ApiResult;
import com.hm.editor.adminservice.datasourceManage.service.DatasourceSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/datasource")
public class DatasourceSetController {
    @Autowired
    DatasourceSetService datasourceSetService;
    @RequestMapping("/set/editorDsSet")
    public ApiResult editorSet(@RequestBody Map d){
        return ApiResult.success(datasourceSetService.editorData(d));
    }
    @RequestMapping("/set/getDsSet")
    public ApiResult getDict(@RequestParam("text")String text,@RequestParam("pageNo")int pageNo,@RequestParam("pageSize")int pageSize){
        return ApiResult.success(datasourceSetService.getData(text,pageNo,pageSize));
    }
    @RequestMapping("/set/delDsSet")
    public ApiResult delDict(@RequestParam("id")String id){
        return ApiResult.success(datasourceSetService.delData(id));
    }
    @RequestMapping("/set/getSetVerData")
    public ApiResult getSetVerData(@RequestParam("dictVerId")String dictVerId){
        return ApiResult.success(datasourceSetService.getVerData(dictVerId));
    }
    @RequestMapping("/set/editorSetVerData")
    public ApiResult editorSetVerData(@RequestBody Map<String,Object> data,@RequestParam("dictVerId")String dictVerId){
        return ApiResult.success(datasourceSetService.editorVerData(data,dictVerId));
    }
    @RequestMapping("/set/delDsSetVerData")
    public ApiResult delDictVerData(@RequestParam("id") String id){
        return ApiResult.success(datasourceSetService.delDictVerData(id));
    }
    // 所有发布的数据集
    @RequestMapping("/set/allPublishedDsSet")
    public ApiResult allPublishedDsSet(){
        return ApiResult.success(datasourceSetService.allPublishedDsSet());
    }

    @RequestMapping("/set/refData")
    public ApiResult refData(@RequestParam String code){
        return ApiResult.success(datasourceSetService.refData(code));
    }
}
