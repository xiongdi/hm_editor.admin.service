package com.hm.editor.adminservice.datasourceManage.controller;

import com.hm.editor.common.dto.ApiResult;
import com.hm.editor.adminservice.datasourceManage.service.DatasourceDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/datasource")
public class DatasourceDictController {
    @Autowired
    DatasourceDictService datasourceDictService;
    @RequestMapping("/dict/editorDict")
    public ApiResult editorDict(@RequestBody Map d){
        return ApiResult.success(datasourceDictService.editorDSDict(d));
    }
    @RequestMapping("/dict/getDict")
    public ApiResult getDict(@RequestParam("text")String text,@RequestParam("pageNo")int pageNo,@RequestParam("pageSize")int pageSize){
        return ApiResult.success(datasourceDictService.getDict(text,pageNo,pageSize));
    }
    @RequestMapping("/dict/delDict")
    public ApiResult delDict(@RequestParam("id")String id){
        return ApiResult.success(datasourceDictService.delDSDict(id));
    }
    @RequestMapping("/dict/exportDict")
    public void exportDict(@RequestParam("text")String text,@RequestParam("pageNo")int pageNo,@RequestParam("pageSize")int pageSize){

        // todo
        //return ApiResult.success(datasourceDictService.getDict(text,pageNo,pageSize));
    }
    @RequestMapping("/dict/allDict")
    public ApiResult allDict(){

        return ApiResult.success(datasourceDictService.allDict());
    }
    // 启用且已发布值域
    @RequestMapping("/dict/allUsedDict")
    public ApiResult allUsedDict(){

        return ApiResult.success(datasourceDictService.allUsedDict());
    }



    @RequestMapping("/dict/getDictVerData")
    public ApiResult getDictVerData(@RequestParam("dictVerId")String dictVerId){
        return ApiResult.success(datasourceDictService.getDictVerData(dictVerId));
    }
    @RequestMapping("/dict/editorDictVerData")
    public ApiResult editorDictVerData(@RequestBody Map<String,Object> data,@RequestParam("dictVerId")String dictVerId){
        return ApiResult.success(datasourceDictService.editorDsDictVerData(data,dictVerId));
    }
    @RequestMapping("/dict/delDictVerData")
    public ApiResult delDictVerData(@RequestParam("id") String id){
        return ApiResult.success(datasourceDictService.delDsDictVerData(id));
    }

    @RequestMapping("/dict/getDictVerDataByCode")
    public ApiResult getDictVerDataByCode(@RequestParam("code") String code){
        return ApiResult.success(datasourceDictService.getDsDictVerDataByCode(code));
    }



    @RequestMapping("/dict/refData")
    public ApiResult refData(@RequestParam("code") String code){
        return ApiResult.success(datasourceDictService.refData(code));
    }



}
