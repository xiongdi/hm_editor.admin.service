package com.hm.editor.adminservice.console.service;

import com.hm.editor.adminservice.console.domain.EmrDataElementAdmin;
import com.hm.editor.adminservice.console.domain.EmrDataSet;
import com.hm.editor.adminservice.console.repository.BaseFolderRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseFolderService {

    @Autowired
    private BaseFolderRepository baseFolderRepository;

    public List<EmrDataSet> getAll() {
        return baseFolderRepository.getAll();
    }

    public List<EmrDataElementAdmin> getCondition(String id) {
        return baseFolderRepository.getCondition(id);
    }

    public Object getDataSet(Map<String, Object> param) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> page = (Map<String, Object>) param.get("page");
        Map r = baseFolderRepository.getDataSet(param);

        int totalRecords = r.containsKey("total") ? (int) r.get("total") : 0;
        page.put("totalRecords", totalRecords);
        res.put("page", page);
        res.put("dataList", r.get("data"));
        return res;
    }

    public boolean delBaseFolder(String id) {
        return baseFolderRepository.delBaseFolder(id);
    }

    public boolean addBaseFolder(Map<String, Object> emrDataSet) {
        return baseFolderRepository.addBaseFolder(emrDataSet);
    }

    public boolean editBaseFolder(Map<String, Object> EmrDataSet) {
        boolean flag = baseFolderRepository.editBaseFolder(EmrDataSet);
        return flag;
    }
}
