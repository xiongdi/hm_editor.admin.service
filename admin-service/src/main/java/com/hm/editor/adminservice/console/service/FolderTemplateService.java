package com.hm.editor.adminservice.console.service;

import com.hm.editor.adminservice.console.domain.EmrBaseFolder;
import com.hm.editor.adminservice.console.domain.EmrBaseTemplate;
import com.hm.editor.adminservice.console.repository.FolderTemplateRepository;
import com.hm.editor.adminservice.console.utils.FolderUtils;
import com.hm.editor.adminservice.datasourceManage.service.DatasourceSetService;
import com.hm.editor.common.dto.ApiResult;
import java.util.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

/**
 * @PROJECT_NAME:service_spce
 *
 * @author:wanglei
 * @date:2020/11/11 2:04 PM @Description:
 */
@Service
public class FolderTemplateService {

    @Autowired
    FolderTemplateRepository folderTemplateRepository;

    @Autowired
    DatasourceSetService datasourceSetService;

    public Map<String, Object> getAll(Map<String, Object> param) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> page = (Map<String, Object>) param.get("page");

        long total = folderTemplateRepository.getTotal(param);
        page.put("totalRecords", total);
        res.put("page", page);
        if (total == 0) {
            res.put("dataList", new ArrayList<EmrBaseTemplate>());
            return res;
        }
        List<EmrBaseTemplate> data = folderTemplateRepository.getAll(param);
        data.forEach(EmrBaseTemplate::initIDStr);
        res.put("dataList", data);
        return res;
    }

    public boolean delBasTemplate(String id) {
        return folderTemplateRepository.delBasTemplate(id);
    }

    public String editorEmrFolder(EmrBaseTemplate emrBaseTemplate, String hosnum) {
        String name = emrBaseTemplate.getTemplateName();

        String msg = "";
        String idStr;
        if (emrBaseTemplate.get_id() == null) {
            idStr = FolderUtils.uuid(24);
            emrBaseTemplate.setIdStr(idStr);
            msg = "添加失败";
        } else {
            idStr = emrBaseTemplate.getIdStr();
            emrBaseTemplate.setEditDate(new Date());
            msg = "修改失败";
        }
        emrBaseTemplate.set_id(new ObjectId(idStr));
        if (existOwnEmrFolderByName(emrBaseTemplate.getIdStr(), name)) {
            return "已存在病历(" + name + ")";
        }
        boolean flag = folderTemplateRepository.editBasTemplate(emrBaseTemplate);
        return flag ? "" : msg;
    }

    public boolean existOwnEmrFolderByName(String id, String name) {
        return folderTemplateRepository.existOwnFolder(id, name);
    }

    public ApiResult getBasTemplateList(EmrBaseFolder folder) {
        List<EmrBaseTemplate> templates = folderTemplateRepository.getListByFolderId(
            folder.get_id()
        );
        templates.forEach(EmrBaseTemplate::initIDStr);
        return ApiResult.success(templates);
    }

    public ApiResult mapDirTemplate() {
        return ApiResult.success(getAllTemplate());
    }

    public Map<String, Object> getAllTemplate() {
        Map<String, Object> temMap = new HashMap<>();
        Map<String, Set<String>> _f = new HashMap<>();
        Map<String, Map<String, String>> _t = new HashMap<>();
        List<Map<String, Object>> templates = folderTemplateRepository.allTemplate();
        for (Map<String, Object> m : templates) {
            String folderName = m.get("name").toString();
            String templateName = m.get("templateName").toString();
            String inpType = m.get("type").toString();
            String isMore = m.get("isForbidMultiple").toString();

            Set<String> ts = new HashSet<>();
            if (_f.containsKey(folderName)) {
                ts = _f.get(folderName);
            }
            ts.add(templateName);
            _f.put(folderName, ts);

            Map<String, String> tMap = new HashMap<>();
            tMap.put("folder", folderName);
            tMap.put("type", inpType);
            tMap.put("isMore", isMore);
            _t.put(templateName, tMap);
        }

        temMap.put("目录", _f);
        temMap.put("模板", _t);
        return temMap;
    }

    public boolean saveBaseTemplateHtml(String id, String html) {
        return folderTemplateRepository.saveBaseTemplateHtml(id, html);
    }

    public String getBaseTemplateHtml(String id) {
        return folderTemplateRepository.getBaseTemplateHtml(id);
    }

    public List<Map<String, Object>> templateDs(String name) {
        List<EmrBaseTemplate> templates = folderTemplateRepository.getListByCri(
            Criteria.where("templateName").is(name)
        );
        List<String> dsSet;
        if (
            templates.isEmpty() || (dsSet = templates.get(0).getDsSet()) == null || dsSet.isEmpty()
        ) {
            return new ArrayList<>();
        }

        return datasourceSetService.allDsList(dsSet);
    }
}
