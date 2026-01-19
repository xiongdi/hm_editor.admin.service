package com.hm.editor.adminservice.datasourceManage.service;

import com.hm.editor.adminservice.datasourceManage.repository.CommonRepository;
import com.hm.editor.adminservice.datasourceManage.repository.DatasourceDictRepository;
import com.hm.editor.adminservice.datasourceManage.utils.ContantUtil;
import com.hm.editor.adminservice.datasourceManage.utils.DataUtil;
import com.hm.editor.common.utils.HashIdGenerator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

/*
 * 值域
 */
@Service
public class DatasourceDictService {

    private static final String dsDictColName = ContantUtil.DS_DICT_COLLECTION_NAME;
    // private String dsDictVerColName = ContantUtil.DS_DICT_VERSION_COLLECTION_NAME;
    private static final String dsDictVerDataColName =
        ContantUtil.DS_DICT_VARSION_DATA_COLLECTION_NAME;

    @Autowired
    CommonService commonService;

    @Autowired
    CommonRepository commonRepository;

    @Autowired
    DatasourceDictRepository datasourceDictRepository;

    // 值域新增
    public boolean addDSDict(Map<String, Object> data) {
        // 确保不包含_id，明确表示这是新增操作
        data.remove("_id");
        return editorDSDict(data);
    }

    // 值域新增、编辑
    public boolean editorDSDict(Map<String, Object> data) {
        String idKey = "_id";
        if (data.containsKey(idKey)) {
            data.put(idKey, DataUtil.str2ObjectId(data.get(idKey).toString()));
        }
        // 如果code为空或不存在，根据name生成6位code
        if (
            !data.containsKey("code") ||
            data.get("code") == null ||
            (data.get("code") instanceof String && ((String) data.get("code")).trim().isEmpty())
        ) {
            if (data.containsKey("name") && data.get("name") != null) {
                String name = data.get("name").toString();
                String code = HashIdGenerator.generate(name, 6);
                data.put("code", code);
            }
        }
        return commonService.checkAndSave(
            data,
            dsDictColName,
            "code",
            "code",
            "name",
            "status",
            "versionUid"
        );
    }

    public boolean delDSDict(String id) {
        commonService.canDel(id, "值域");
        return commonService.delData(DataUtil.str2ObjectId(id), dsDictColName);
    }

    public Map<String, Object> getDict(String text, int pageNo, int pageSize) {
        Map<String, Object> d = commonService.pageData(
            dsDictColName,
            text,
            pageNo,
            pageSize,
            "code",
            "code",
            "name"
        );
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> data = (List<Map<String, Object>>) d.get("data");
        final Map<String, Object> codeCount;
        if (!data.isEmpty()) {
            List<String> codes = data
                .stream()
                .map(_d -> _d.get("code").toString())
                .collect(Collectors.toList());

            List<Map<String, Object>> ref = commonService.refCount("数据元", codes);
            codeCount = ref
                .stream()
                .collect(
                    Collectors.toMap(_d -> _d.get("_id").toString(), _d -> _d.get("count"), (k1, k2) -> k1)
                );
        } else {
            codeCount = new HashMap<>();
        }

        data.forEach(_d -> {
            DataUtil.objectId2Str(_d);
            _d.put(
                "showStatus",
                (_d.get("status") != null && (boolean) _d.get("status")) ? "是" : "否"
            );
            _d.put("count", codeCount.getOrDefault(_d.get("code"), ""));
        });

        return d;
    }

    public List<Map<String, Object>> allDict() {
        List<Map<String, Object>> d = commonRepository.find(dsDictColName, new Criteria(), Sort.by("code"));
        DataUtil.objectId2Str(d);
        return d;
    }

    public List<Map<String, Object>> allUsedDict() {
        List<Map<String, Object>> d = commonRepository.find(
            dsDictColName,
            Criteria.where("status").is(true),
            Sort.by("code")
        );
        DataUtil.objectId2Str(d);
        return d;
    }

    public List<Map<String, Object>> getDictVerData(String dictVerId) {
        List<Map<String, Object>> d = commonRepository.find(
            dsDictVerDataColName,
            Criteria.where("dictVersionUid").is(dictVerId),
            Sort.by("order")
        );
        DataUtil.objectId2Str(d, "_id");
        return d;
    }

    public List<Map<String, Object>> editorDsDictVerData(Map<String, Object> d, String dictVerId) {
        d.put("dictVersionUid", dictVerId);
        commonRepository.save(d, dsDictVerDataColName, d.keySet().toArray(new String[d.size()]));
        return getDictVerData(dictVerId);
    }

    public boolean delDsDictVerData(String id) {
        return commonService.delData(DataUtil.str2ObjectId(id), dsDictVerDataColName);
    }

    public List<Map<String, Object>> getDsDictVerDataByCode(String code) {
        return datasourceDictRepository.getDictVerDataByCode(code);
    }

    public List<Map<String, Object>> refData(String code) {
        String type = "数据元";
        return commonService.refList(type, code);
    }
}
