package com.hm.editor.adminservice.datasourceManage.service;

import com.hm.editor.adminservice.datasourceManage.repository.CommonRepository;
import com.hm.editor.adminservice.datasourceManage.repository.DatasourceDictRepository;
import com.hm.editor.adminservice.datasourceManage.repository.DatasourceSetRepository;
import com.hm.editor.adminservice.datasourceManage.utils.ContantUtil;
import com.hm.editor.adminservice.datasourceManage.utils.DataUtil;
import com.hm.editor.exception.BusinessException;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

/*
 * 值域
 */
@Service
public class DatasourceSetService {
  private String colName = ContantUtil.DS_SET_COLLECTION_NAME;
  // private String verColName = ContantUtil.DS_SET_VERSION_COLLECTION_NAME;
  // private String verDataColName = ContantUtil.DS_SET_VARSION_DATA_COLLECTION_NAME;
  @Autowired CommonService commonService;

  @Autowired CommonRepository commonRepository;

  @Autowired DatasourceSetRepository datasourceSetRepository;

  @Autowired DatasourceDictRepository datasourceDictRepository;

  // 值域新增、编辑
  public Map editorData(Map<String, Object> data) {
    String idKey = "_id";
    boolean addFlag = !data.containsKey(idKey);
    if (!addFlag) {
      data.put(idKey, DataUtil.str2ObjectId(data.get(idKey).toString()));
    }
    boolean flag = commonService.checkAndSave(data, colName, "code", "code", "name", "versionUid");
    Map res = new HashMap();
    res.put("editFlag", flag);
    if (flag && addFlag) {
      List<Map> all =
          commonRepository.find(colName, new Criteria(), Sort.by(Sort.Direction.ASC, "code"));
      int addIndex = 0;
      for (Map m : all) {
        if (data.get("code").equals(m.get("code"))) {
          res.put("index", addIndex);
          DataUtil.objectId2StrByKey(m, "_id");
          res.put("addData", m);
        }
        addIndex++;
      }
    }
    return res;
  }

  public boolean delData(String id) {

    commonService.canDel(id, "数据集");
    Map s = commonRepository.findOne(colName, Criteria.where("_id").is(DataUtil.str2ObjectId(id)));
    List<Map> d =
        commonRepository.find("emrBaseTemplate", Criteria.where("dsSet").is(s.get("code")), null);
    if (!d.isEmpty()) {
      throw new BusinessException("数据集已被【" + d.get(0).get("templateName") + "】引用，请先取消引用");
    }
    commonService.delData(DataUtil.str2ObjectId(id), colName);
    if (s != null && s.get("code") != null) {
      commonRepository.delData(
          Criteria.where("code").is(s.get("code")), ContantUtil.DS_REFRENCE_COLLECTION_NAME);
    }
    return true;
  }

  public Map getData(String text, int pageNo, int pageSize) {

    Map d = commonService.pageData(colName, text, pageNo, pageSize, "code", "code", "name");
    List<Map> data = (List) d.get("data");
    Set<String> codes =
        data.stream()
            .filter(_d -> _d.get("code") != null && !_d.get("code").toString().isEmpty())
            .map(_d -> _d.get("code").toString())
            .collect(Collectors.toSet());
    Map<String, List<Map>> codeRef = refData(codes);

    data.forEach(
        _d -> {
          DataUtil.objectId2Str(_d);
          _d.put(
              "count",
              codeRef.containsKey(_d.get("code")) ? codeRef.get(_d.get("code")).size() : "");
        });

    return d;
  }

  //
  public Map getVerData(String dictVerId) {

    Map res = new HashMap();
    List<Map> d = datasourceSetRepository.getVerData(dictVerId);
    d =
        d.stream()
            .filter(_d -> _d.containsKey("gp") || _d.containsKey("ds"))
            .collect(Collectors.toList());
    // DataUtil.objectId2Str(d,"_id");
    List<String> gcode = new ArrayList<>();
    for (Map dd : d) {
      DataUtil.objectId2Str(dd);
      Object l = dd.get("ds");
      if (l != null && l instanceof Map && !((Map<?, ?>) l).isEmpty()) {
        dd.put("name", ((Map<?, ?>) l).get("name"));
      }
    }

    res.put("data", d);
    return res;
  }

  public Map editorVerData(Map<String, Object> d, String dictVerId) {
    Map<String, Object> d1 = new HashMap();
    d1.put("_id", d.get("_id"));
    d1.put("type", "数据集");
    d1.put("code", dictVerId);
    d1.put("refCode", d.get("code"));
    d1.put("remark", d.get("type"));
    d1.put("createTime", new Date());
    commonRepository.save(
        d1, ContantUtil.DS_REFRENCE_COLLECTION_NAME, d1.keySet().toArray(new String[d.size()]));
    return getVerData(dictVerId);
  }

  public boolean delDictVerData(String id) {
    return commonService.delData(
        DataUtil.str2ObjectId(id), ContantUtil.DS_REFRENCE_COLLECTION_NAME);
  }

  public List<Map> allPublishedDsSet() {
    List<Map> data = commonRepository.find(colName, new Criteria(), null);

    DataUtil.objectId2Str(data);
    return data;
  }

  public List<Map> allDsList(List<String> setCode) {
    List<Map> l = datasourceSetRepository.getDsSetVerData(setCode);

    Set<String> dsCode =
        l.stream()
            .filter(ll -> "数据元".equals(ll.get("remark")))
            .map(ll -> ll.get("refCode").toString())
            .collect(Collectors.toSet());
    Set<String> allDsCode = new HashSet<>();

    if (!dsCode.isEmpty()) {
      allDsCode.addAll(dsCode);
    }
    if (allDsCode.isEmpty()) {
      return new ArrayList<>();
    }
    List<Map> _ds =
        commonRepository.find(
            ContantUtil.DS_COLLECTION_NAME, Criteria.where("code").in(allDsCode), null);
    Set<String> dictCode =
        _ds.stream()
            .filter(d -> d.get("dictCode") != null && d.get("dictCode").toString().length() > 0)
            .map(d -> d.get("dictCode").toString())
            .collect(Collectors.toSet());
    if (!dictCode.isEmpty()) {
      List<Map> dicts =
          datasourceDictRepository.getDictVerDataByCodes(
              dictCode.toArray(new String[dictCode.size()]));
      Map<String, List<Map>> dg =
          dicts.stream().collect(Collectors.groupingBy(d -> d.get("code").toString()));

      _ds.forEach(
          _d -> {
            if (_d.containsKey("dictCode") && dg.containsKey(_d.get("dictCode"))) {
              _d.put("dictList", dg.get(_d.get("dictCode")));
            }
          });
    }
    return _ds;
  }

  public List<Map> refData(String code) {
    List<Map> d = commonRepository.find("emrBaseTemplate", Criteria.where("dsSet").is(code), null);

    d.forEach(_d -> _d.put("name", _d.get("templateName")));
    return d;
  }

  public Map<String, List<Map>> refData(Set<String> code) {
    Map<String, List<Map>> groupByCode = new HashMap<>();
    if (code == null || code.isEmpty()) {
      return groupByCode;
    }
    Criteria cri = new Criteria();
    cri.orOperator(
        code.stream().map(c -> Criteria.where("dsCode").is(c)).collect(Collectors.toSet()));
    List<Map> d = commonRepository.find("emrBaseTemplate", Criteria.where("dsSet").in(code), null);
    d.forEach(_d -> _d.put("name", _d.get("templateName")));

    for (Map dd : d) {
      Object dsSet = dd.get("dsSet");
      if (dsSet != null && dsSet instanceof Collection) {
        Collection<String> _dses = (Collection) dsSet;
        Set<String> dses = new HashSet<>(_dses);
        for (String ds : dses) {
          List<Map> dsL = groupByCode.getOrDefault(ds, new ArrayList<>());
          dsL.add(dd);
          groupByCode.put(ds, dsL);
        }
      }
    }

    return groupByCode;
  }
}
