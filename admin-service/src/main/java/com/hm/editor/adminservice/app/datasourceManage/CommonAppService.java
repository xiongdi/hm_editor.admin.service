package com.hm.editor.adminservice.app.datasourceManage;

import com.hm.editor.adminservice.domain.datasourceManage.gateway.CommonGateway;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 通用应用服务
 */
@Service
public class CommonAppService {

    @Autowired
    private CommonGateway commonGateway;

    public boolean checkAndSave(
        Map<String, Object> d,
        String colName,
        String uniqueKey,
        String... valueKey
    ) {
        return commonGateway.checkAndSave(d, colName, uniqueKey, valueKey);
    }

    public boolean delData(String id, String colName) {
        return commonGateway.delData(id, colName);
    }

    public Map<String, Object> pageData(
        String colName,
        String txt,
        int pageNo,
        int pageSize,
        String sortKey,
        String... key
    ) {
        return commonGateway.pageData(colName, txt, pageNo, pageSize, sortKey, key);
    }

    public Map<String, Object> pageData(
        String colName,
        Object criteria,
        int pageNo,
        int pageSize,
        String sortKey
    ) {
        return commonGateway.pageData(colName, criteria, pageNo, pageSize, sortKey);
    }

    public List<Map<String, Object>> getAllRef(String type, String... code) {
        return commonGateway.getAllRef(type, code);
    }

    public boolean doRefence(String id, String type, String code, String refCode) {
        return commonGateway.doRefence(id, type, code, refCode);
    }

    public boolean doRefence(String type, String code, String refCode) {
        return commonGateway.doRefence(type, code, refCode);
    }

    public boolean removeRefence(String type, String code, String refCode) {
        return commonGateway.removeRefence(type, code, refCode);
    }

    public boolean canDel(String id, String type) {
        return commonGateway.canDel(id, type);
    }

    public List<Map<String, Object>> refList(String type, String refCode) {
        return commonGateway.refList(type, refCode);
    }

    public long refCount(String type, String refCode) {
        return commonGateway.refCount(type, refCode);
    }

    public List<Map<String, Object>> refCount(String type, List<String> refCode) {
        return commonGateway.refCount(type, refCode);
    }
}
