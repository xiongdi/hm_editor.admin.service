package com.hm.editor.adminservice.app.datasourceManage;

import com.hm.editor.adminservice.domain.datasourceManage.gateway.DatasourceGateway;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据源应用服务
 */
@Service
public class DatasourceAppService {

    @Autowired
    private DatasourceGateway datasourceGateway;

    public boolean addDS(Map<String, Object> data) {
        return datasourceGateway.addDS(data);
    }

    public boolean editorDS(Map<String, Object> data) {
        return datasourceGateway.editorDS(data);
    }

    public boolean delDs(String id) {
        return datasourceGateway.delDs(id);
    }

    public Map<String, Object> getDs(String text, int pageNo, int pageSize) {
        return datasourceGateway.getDs(text, pageNo, pageSize);
    }

    public List<Map<String, Object>> getAllDs() {
        return datasourceGateway.getAllDs();
    }

    public List<Map<String, Object>> refData(String code) {
        return datasourceGateway.refData(code);
    }

    public String generalCode(String name) {
        return datasourceGateway.generalCode(name);
    }
}
