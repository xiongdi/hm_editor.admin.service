package com.hm.editor.adminservice.datasourceManage.service;

import com.hm.editor.adminservice.datasourceManage.repository.CommonRepository;
import com.hm.editor.adminservice.datasourceManage.repository.DatasourceDictRepository;
import com.hm.editor.adminservice.datasourceManage.utils.ContantUtil;
import com.hm.editor.adminservice.datasourceManage.utils.DataUtil;
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
  private String dsDictColName = ContantUtil.DS_DICT_COLLECTION_NAME;
  // private String dsDictVerColName = ContantUtil.DS_DICT_VERSION_COLLECTION_NAME;
  private String dsDictVerDataColName = ContantUtil.DS_DICT_VARSION_DATA_COLLECTION_NAME;
  @Autowired CommonService commonService;

  @Autowired CommonRepository commonRepository;

  @Autowired DatasourceDictRepository datasourceDictRepository;

  // 值域新增、编辑
  public boolean editorDSDict(Map<String, Object> data) {
    String idKey = "_id";
    if (data.containsKey(idKey)) {
      data.put(idKey, DataUtil.str2ObjectId(data.get(idKey).toString()));
    }
    return commonService.checkAndSave(
        data, dsDictColName, "code", "code", "name", "status", "versionUid");
  }

  public boolean delDSDict(String id) {
    commonService.canDel(id, "值域");
    return commonService.delData(DataUtil.str2ObjectId(id), dsDictColName);
  }

  public Map getDict(String text, int pageNo, int pageSize) {

    Map d = commonService.pageData(dsDictColName, text, pageNo, pageSize, "code", "code", "name");
    List<Map> data = (List) d.get("data");
    final Map codeCount;
    if (!data.isEmpty()) {

      List<String> codes =
          data.stream().map(_d -> _d.get("code").toString()).collect(Collectors.toList());

      List<Map> ref = commonService.refCount("数据元", codes);
      codeCount =
          ref.stream()
              .collect(
                  Collectors.toMap(_d -> _d.get("_id"), _d -> _d.get("count"), (k1, k2) -> k1));
    } else {
      codeCount = new HashMap();
    }

    data.forEach(
        _d -> {
          DataUtil.objectId2Str(_d);
          _d.put(
              "showStatus", (_d.get("status") != null && (boolean) _d.get("status")) ? "是" : "否");
          _d.put("count", codeCount.getOrDefault(_d.get("code"), ""));
        });

    return d;
  }

  public List<Map> allDict() {
    List<Map> d = commonRepository.find(dsDictColName, new Criteria(), Sort.by("code"));
    DataUtil.objectId2Str(d);
    return d;
  }

  public List<Map> allUsedDict() {
    List<Map> d =
        commonRepository.find(dsDictColName, Criteria.where("status").is(true), Sort.by("code"));
    DataUtil.objectId2Str(d);
    return d;
  }

  public List<Map> getDictVerData(String dictVerId) {
    List<Map> d =
        commonRepository.find(
            dsDictVerDataColName, Criteria.where("dictVersionUid").is(dictVerId), Sort.by("order"));
    DataUtil.objectId2Str(d, "_id");
    return d;
  }

  public List<Map> editorDsDictVerData(Map<String, Object> d, String dictVerId) {
    d.put("dictVersionUid", dictVerId);
    commonRepository.save(d, dsDictVerDataColName, d.keySet().toArray(new String[d.size()]));
    return getDictVerData(dictVerId);
  }

  public boolean delDsDictVerData(String id) {
    return commonService.delData(DataUtil.str2ObjectId(id), dsDictVerDataColName);
  }

  public List<Map> getDsDictVerDataByCode(String code) {

    return datasourceDictRepository.getDictVerDataByCode(code);
  }

  public List<Map> refData(String code) {
    String type = "数据元";
    return commonService.refList(type, code);
  }
}
