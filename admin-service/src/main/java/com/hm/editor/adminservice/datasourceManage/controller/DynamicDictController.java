package com.hm.editor.adminservice.datasourceManage.controller;

import com.hm.editor.common.dto.ApiResult;
import com.hm.editor.adminservice.datasourceManage.service.DynamicDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/datasource")
public class DynamicDictController {
    @Autowired
    DynamicDictService dynamicDictService;

    @RequestMapping("/dynamicDict/editorDynamicDict")
    public ApiResult editorDynamicDict(@RequestBody Map d){
        try{
            return ApiResult.success(dynamicDictService.editorDynamicDict(d));
        }catch (Throwable e){
            return ApiResult.failed(e.getMessage());
        }

    }

    @RequestMapping("/dynamicDict/getDynamicDict")
    public ApiResult getDynamicDict(@RequestParam("text") String text,
                                          @RequestParam("pageNo") int pageNo,
                                          @RequestParam("pageSize") int pageSize){
        return ApiResult.success(dynamicDictService.getDynamicDict(text, pageNo, pageSize));
    }

    /**
     * 逻辑删除动态值域（设置isDeleted=1）
     * 只是标记删除状态，不会物理删除数据
     */
    @RequestMapping("/dynamicDict/delDynamicDict")
    public ApiResult delDynamicDict(@RequestParam("id") String id){
        return ApiResult.success(dynamicDictService.delDynamicDict(id));
    }

    @RequestMapping("/dynamicDict/allDynamicDict")
    public ApiResult allDynamicDict(){
        return ApiResult.success(dynamicDictService.allDynamicDict());
    }

    @RequestMapping("/dynamicDict/getDynamicDictByCode")
    public ApiResult getDynamicDictByCode(@RequestParam("code") String code){
        return ApiResult.success(dynamicDictService.getDynamicDictByCode(code));
    }
}
