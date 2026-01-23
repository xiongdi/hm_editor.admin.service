package com.hm.editor.adminservice.app.console;

import com.hm.editor.adminservice.domain.console.gateway.BaseFolderGateway;
import com.hm.editor.adminservice.domain.console.model.EmrDataElementAdmin;
import com.hm.editor.adminservice.domain.console.model.EmrDataSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 基础目录应用服务
 */
@Service
public class BaseFolderAppService {

    @Autowired
    private BaseFolderGateway baseFolderGateway;

    public List<EmrDataSet> getAll() {
        return baseFolderGateway.getAll();
    }

    public List<EmrDataElementAdmin> getCondition(String id) {
        return baseFolderGateway.getCondition(id);
    }

    public Object getDataSet(Map<String, Object> param) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> page = (Map<String, Object>) param.get("page");
        Map<String, Object> r = baseFolderGateway.getDataSet(param);

        int totalRecords = r.containsKey("total") ? (int) r.get("total") : 0;
        page.put("totalRecords", totalRecords);
        res.put("page", page);
        res.put("dataList", r.get("data"));
        return res;
    }

    public boolean delBaseFolder(String id) {
        return baseFolderGateway.deleteBaseFolder(id);
    }

    public boolean addBaseFolder(Map<String, Object> emrDataSet) {
        return baseFolderGateway.addBaseFolder(emrDataSet);
    }

    public boolean editBaseFolder(Map<String, Object> EmrDataSet) {
        return baseFolderGateway.editBaseFolder(EmrDataSet);
    }
}
