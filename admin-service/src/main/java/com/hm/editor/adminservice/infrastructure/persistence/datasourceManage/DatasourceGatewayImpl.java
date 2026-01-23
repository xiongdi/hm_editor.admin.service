package com.hm.editor.adminservice.infrastructure.persistence.datasourceManage;

import com.hm.editor.adminservice.domain.datasourceManage.gateway.CommonGateway;
import com.hm.editor.adminservice.domain.datasourceManage.gateway.DatasourceGateway;
import com.hm.editor.adminservice.infrastructure.utils.ContantUtil;
import com.hm.editor.adminservice.infrastructure.utils.DataUtil;
import com.hm.editor.adminservice.infrastructure.utils.StringUtils;
import com.hm.editor.adminservice.infrastructure.exception.BusinessException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

/**
 * 数据源Gateway实现
 */
@Component
public class DatasourceGatewayImpl implements DatasourceGateway {

    private static final String datasourceColName = ContantUtil.DS_COLLECTION_NAME;
    private static final String DEFINED_DS_PRE = "DE99.09";

    @Autowired
    private CommonGateway commonGateway;

    @Autowired
    private CommonGatewayImpl commonGatewayImpl;

    @Override
    public boolean addDS(Map<String, Object> data) {
        data.remove("_id");
        return editorDS(data);
    }

    @Override
    public boolean editorDS(Map<String, Object> data) {
        String idKey = "_id";
        if (data.containsKey(idKey)) {
            data.put(idKey, DataUtil.str2ObjectId(data.get(idKey).toString()));
        }
        data.put("createTime", new Date());
        boolean flag = commonGateway.checkAndSave(
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
            commonGateway.doRefence("数据元", data.get("code").toString(), refCode.toString());
        } else {
            commonGateway.removeRefence("数据元", data.get("code").toString(), null);
        }
        return flag;
    }

    @Override
    public boolean delDs(String id) {
        commonGateway.canDel(id, "数据元");
        Map<String, Object> d = commonGatewayImpl.findOne(
            datasourceColName,
            Criteria.where("_id").is(DataUtil.str2ObjectId(id))
        );
        commonGateway.delData(id, datasourceColName);
        if (d != null && d.get("code") != null) {
            commonGatewayImpl.delData(
                Criteria.where("code").is(d.get("code")),
                ContantUtil.DS_REFRENCE_COLLECTION_NAME
            );
            commonGatewayImpl.delData(
                Criteria.where("refCode").is(d.get("code")),
                ContantUtil.DS_REFRENCE_COLLECTION_NAME
            );
        }
        return true;
    }

    @Override
    public Map<String, Object> getDs(String text, int pageNo, int pageSize) {
        Map<String, Object> d = commonGateway.pageData(
            datasourceColName,
            text,
            pageNo,
            pageSize,
            "createTime",
            "code",
            "name"
        );
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> data = (List<Map<String, Object>>) d.get("data");
        data.forEach(DataUtil::objectId2Str);

        return d;
    }

    @Override
    public List<Map<String, Object>> getAllDs() {
        return commonGatewayImpl.find(datasourceColName, new Criteria(), Sort.by("code"));
    }

    @Override
    public List<Map<String, Object>> refData(String code) {
        List<Map<String, Object>> d1 = commonGateway.refList("数据集", code);
        d1.forEach(d -> d.put("type", "数据集"));

        return d1;
    }

    @Override
    public String generalCode(String name) {
        if (commonGatewayImpl.count(Criteria.where("name").is(name), datasourceColName) > 0) {
            throw new BusinessException("已存在【" + name + "】此数据元");
        }

        Object code = commonGatewayImpl.maxVal(
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
