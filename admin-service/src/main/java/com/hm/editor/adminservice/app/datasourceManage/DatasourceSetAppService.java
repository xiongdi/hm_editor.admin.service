package com.hm.editor.adminservice.app.datasourceManage;

import com.hm.editor.adminservice.domain.datasourceManage.gateway.DatasourceSetGateway;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据集应用服务
 */
@Service
public class DatasourceSetAppService {

    @Autowired
    private DatasourceSetGateway datasourceSetGateway;

    public Map<String, Object> addData(Map<String, Object> data) {
        return datasourceSetGateway.addData(data);
    }

    public Map<String, Object> editorData(Map<String, Object> data) {
        return datasourceSetGateway.editorData(data);
    }

    public boolean delData(String id) {
        return datasourceSetGateway.delData(id);
    }

    public Map<String, Object> getData(String text, int pageNo, int pageSize) {
        return datasourceSetGateway.getData(text, pageNo, pageSize);
    }

    public Map<String, Object> getVerData(String dictVerId) {
        return datasourceSetGateway.getVerData(dictVerId);
    }

    public Map<String, Object> editorVerData(Map<String, Object> d, String dictVerId) {
        return datasourceSetGateway.editorVerData(d, dictVerId);
    }

    public boolean delDictVerData(String id) {
        return datasourceSetGateway.delDictVerData(id);
    }

    public List<Map<String, Object>> allPublishedDsSet() {
        return datasourceSetGateway.allPublishedDsSet();
    }

    public List<Map<String, Object>> allDsList(List<String> setCode) {
        return datasourceSetGateway.allDsList(setCode);
    }

    public List<Map<String, Object>> refData(String code) {
        return datasourceSetGateway.refData(code);
    }

    public Map<String, List<Map<String, Object>>> refData(Set<String> code) {
        return datasourceSetGateway.refData(code);
    }
}
