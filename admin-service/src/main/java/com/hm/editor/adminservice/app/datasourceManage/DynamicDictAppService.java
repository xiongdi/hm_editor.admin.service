package com.hm.editor.adminservice.app.datasourceManage;

import com.hm.editor.adminservice.domain.datasourceManage.gateway.DynamicDictGateway;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 动态值域应用服务
 */
@Service
public class DynamicDictAppService {

    @Autowired
    private DynamicDictGateway dynamicDictGateway;

    public boolean addDynamicDict(Map<String, Object> data) throws Exception {
        return dynamicDictGateway.addDynamicDict(data);
    }

    public boolean editorDynamicDict(Map<String, Object> data) throws Exception {
        return dynamicDictGateway.editorDynamicDict(data);
    }

    public boolean delDynamicDict(String id) {
        return dynamicDictGateway.delDynamicDict(id);
    }

    public Map<String, Object> getDynamicDict(String text, int pageNo, int pageSize) {
        return dynamicDictGateway.getDynamicDict(text, pageNo, pageSize);
    }

    public List<Map<String, Object>> allDynamicDict() {
        return dynamicDictGateway.allDynamicDict();
    }

    public Map<String, Object> getDynamicDictByCode(String code) {
        return dynamicDictGateway.getDynamicDictByCode(code);
    }

    public List<Map<String, Object>> findByCode(String code) {
        return dynamicDictGateway.findByCode(code);
    }
}
