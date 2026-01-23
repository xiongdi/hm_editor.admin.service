package com.hm.editor.adminservice.app.datasourceManage;

import com.hm.editor.adminservice.domain.datasourceManage.gateway.DatasourceDictGateway;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据源字典应用服务
 */
@Service
public class DatasourceDictAppService {

    @Autowired
    private DatasourceDictGateway datasourceDictGateway;

    public boolean addDSDict(Map<String, Object> data) {
        return datasourceDictGateway.addDSDict(data);
    }

    public boolean editorDSDict(Map<String, Object> data) {
        return datasourceDictGateway.editorDSDict(data);
    }

    public boolean delDSDict(String id) {
        return datasourceDictGateway.delDSDict(id);
    }

    public Map<String, Object> getDict(String text, int pageNo, int pageSize) {
        return datasourceDictGateway.getDict(text, pageNo, pageSize);
    }

    public List<Map<String, Object>> allDict() {
        return datasourceDictGateway.allDict();
    }

    public List<Map<String, Object>> allUsedDict() {
        return datasourceDictGateway.allUsedDict();
    }

    public List<Map<String, Object>> getDictVerData(String dictVerId) {
        return datasourceDictGateway.getDictVerData(dictVerId);
    }

    public List<Map<String, Object>> editorDsDictVerData(Map<String, Object> d, String dictVerId) {
        return datasourceDictGateway.editorDsDictVerData(d, dictVerId);
    }

    public boolean delDsDictVerData(String id) {
        return datasourceDictGateway.delDsDictVerData(id);
    }

    public List<Map<String, Object>> getDsDictVerDataByCode(String code) {
        return datasourceDictGateway.getDsDictVerDataByCode(code);
    }

    public List<Map<String, Object>> refData(String code) {
        return datasourceDictGateway.refData(code);
    }
}
