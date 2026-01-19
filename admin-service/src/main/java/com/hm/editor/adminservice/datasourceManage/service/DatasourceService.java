package com.hm.editor.adminservice.datasourceManage.service;

import com.hm.editor.adminservice.datasourceManage.repository.CommonRepository;
import com.hm.editor.adminservice.datasourceManage.utils.ContantUtil;
import com.hm.editor.adminservice.datasourceManage.utils.DataUtil;
import com.hm.editor.common.utils.StringUtils;
import com.hm.editor.exception.BusinessException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

@Service
public class DatasourceService {

    private static final String datasourceColName = ContantUtil.DS_COLLECTION_NAME;
    private static final String DEFINED_DS_PRE = "DE99.09";

    @Autowired
    CommonService commonService;

    @Autowired
    CommonRepository commonRepository;

    // 新增数据源
    public boolean addDS(Map<String, Object> data) {
        // 确保不包含_id，明确表示这是新增操作
        data.remove("_id");
        return editorDS(data);
    }

    // 新增、编辑
    public boolean editorDS(Map<String, Object> data) {
        String idKey = "_id";
        if (data.containsKey(idKey)) {
            data.put(idKey, DataUtil.str2ObjectId(data.get(idKey).toString()));
        }
        data.put("createTime", new Date());
        boolean flag = commonService.checkAndSave(
            data,
            datasourceColName,
            "code",
            "code",
            "name",
            "description",
            "type",
            "format",
            "length",
            "dictCode",
            "createTime"
        );
        Object refCode = data.get("dictCode");
        if (!StringUtils.isNull(refCode)) {
            commonService.doRefence("数据元", data.get("code").toString(), refCode.toString());
        } else {
            commonService.removeRefence("数据元", data.get("code").toString(), null);
        }
        return flag;
    }

    public boolean delDs(String id) {
        commonService.canDel(id, "数据元");
        Map d = commonRepository.findOne(
            datasourceColName,
            Criteria.where("_id").is(DataUtil.str2ObjectId(id))
        );
        commonService.delData(DataUtil.str2ObjectId(id), datasourceColName);
        if (d != null && d.get("code") != null) {
            commonRepository.delData(
                Criteria.where("code").is(d.get("code")),
                ContantUtil.DS_REFRENCE_COLLECTION_NAME
            );
            commonRepository.delData(
                Criteria.where("refCode").is(d.get("code")),
                ContantUtil.DS_REFRENCE_COLLECTION_NAME
            );
        }
        return true;
    }

    public Map getDs(String text, int pageNo, int pageSize) {
        Map d = commonService.pageData(
            datasourceColName,
            text,
            pageNo,
            pageSize,
            "code",
            "code",
            "name"
        );
        List<Map> data = (List) d.get("data");
        data.forEach(DataUtil::objectId2Str);

        return d;
    }

    public List<Map> getAllDs() {
        return commonRepository.find(datasourceColName, new Criteria(), Sort.by("code"));
    }

    public List<Map> refData(String code) {
        List<Map> d1 = commonService.refList("数据集", code);
        d1.forEach(d -> d.put("type", "数据集"));

        return d1;
    }

    public String generalCode(String name) {
        if (commonRepository.count(Criteria.where("name").is(name), datasourceColName) > 0) {
            throw new BusinessException("已存在【" + name + "】此数据元");
        }

        Object code = commonRepository.maxVal(
            datasourceColName,
            Criteria.where("code").regex(DEFINED_DS_PRE.replaceAll("\\.", "\\\\.")),
            "code"
        );
        int index = 1;
        if (code != null && !code.toString().isEmpty()) {
            index =
                Integer.parseInt(
                    code.toString().replace(DEFINED_DS_PRE, "").replace(".", "").trim()
                ) +
                1;
        }

        StringBuilder sb = new StringBuilder();

        int addLen = 5 - String.valueOf(index).length();
        for (int i = 0; i < addLen; i++) {
            sb.append("0");
        }
        sb.append(index);
        String s = sb.toString();
        return DEFINED_DS_PRE + "." + s.substring(0, 3) + "." + s.substring(3);
    }
}
