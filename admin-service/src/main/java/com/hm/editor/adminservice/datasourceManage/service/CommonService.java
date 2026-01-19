package com.hm.editor.adminservice.datasourceManage.service;

import com.hm.editor.adminservice.datasourceManage.repository.CommonRepository;
import com.hm.editor.adminservice.datasourceManage.utils.ContantUtil;
import com.hm.editor.common.utils.EscapeUtil;
import com.hm.editor.common.utils.StringUtils;
import com.hm.editor.exception.BusinessException;
import java.util.*;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class CommonService {
  @Autowired CommonRepository commonRepository;

  public boolean checkAndSave(Map d, String colName, String uniqueKey, String... valueKey) {
    if (d == null || d.isEmpty()) {
      throw new BusinessException("数据异常");
    }

    Object _id = d.get("_id");
    Criteria cri = Criteria.where(uniqueKey).is(d.get(uniqueKey));
    if (_id != null) {
      cri.and("_id").ne(_id);
    }
    if (commonRepository.exists(cri, colName)) {
      throw new BusinessException("存在【" + d.get(valueKey[0]) + "】的数据");
    }

    commonRepository.save(d, colName, valueKey);
    return true;
  }

  public boolean delData(ObjectId id, String colName) {
    return commonRepository.delData(id, colName);
  }

  public Map pageData(
      String colName, String txt, int pageNo, int pageSize, String sortKey, String... key) {
    Map res = new HashMap();
    Criteria criteria = new Criteria();
    if (StringUtils.isNotBlank(txt)) {
      criteria.orOperator(
          Arrays.stream(key)
              .map(s -> Criteria.where(s).regex(EscapeUtil.escapeExprSpecialWord(txt)))
              .collect(Collectors.toList()));
    }
    res.put("total", commonRepository.count(criteria, colName));
    List<Map> data = new ArrayList<>();
    if ((long) res.get("total") > 0) {
      Sort sort = null;
      if (StringUtils.isNotBlank(sortKey)) {
        sort = Sort.by(sortKey);
      }
      data = commonRepository.pageData(colName, criteria, sort, pageNo, pageSize);
    }

    res.put("data", data);
    return res;
  }

  /**
   * 分页查询数据
   *
   * @param colName 集合名称
   * @param criteria 查询条件
   * @param pageNo 当前页码
   * @param pageSize 每页记录数
   * @return 分页结果
   */
  public Map pageData(String colName, Criteria criteria, int pageNo, int pageSize, String sortKey) {
    Map res = new HashMap();
    res.put("total", commonRepository.count(criteria, colName));
    List<Map> data = new ArrayList<>();
    if ((long) res.get("total") > 0) {
      Sort sort = null;
      if (StringUtils.isNotBlank(sortKey)) {
        sort = Sort.by(sortKey);
      }
      data = commonRepository.pageData(colName, criteria, sort, pageNo, pageSize);
    }

    res.put("data", data);
    return res;
  }

  //    public List<Map> getAllRef(String type,String code){
  //        return
  // commonRepository.find(ContantUtil.DS_REFRENCE_COLLECTION_NAME,Criteria.where("type").is(type).and("code").is(code),Sort.by("createTime"));
  //    }
  public List<Map> getAllRef(String type, String... code) {
    Criteria cri = Criteria.where("type").is(type).and("code").in(code);
    String name = "";
    if ("数据集".equals(type)) {
      // todo
      name = ContantUtil.DS_COLLECTION_NAME;
    } else if ("数据元".equals(type)) {
      name = ContantUtil.DS_DICT_COLLECTION_NAME;
    }
    return getOneGroupRef(cri, name);
  }

  private List<Map> getOneGroupRef(Criteria cri, String refName) {
    List<AggregationOperation> aggs = new ArrayList<>();
    aggs.add(Aggregation.match(cri));

    aggs.add(Aggregation.lookup(refName, "refCode", "code", "r1"));
    aggs.add(Aggregation.unwind("r1"));
    aggs.add(Aggregation.sort(Sort.Direction.ASC, "createTime"));
    aggs.add(Aggregation.project("_id", "code", "refCode", "r1.name"));
    aggs.add(Aggregation.match(Criteria.where("name").ne(null)));
    return commonRepository.agg(aggs, ContantUtil.DS_REFRENCE_COLLECTION_NAME);
  }

  public boolean doRefence(ObjectId id, String type, String code, String refCode) {
    Map d = new HashMap();
    d.put("_id", id);
    d.put("type", type);
    d.put("code", code);
    d.put("refCode", refCode);
    d.put("createTime", new Date());
    commonRepository.save(
        d, ContantUtil.DS_REFRENCE_COLLECTION_NAME, "type", "code", "refCode", "createTime");
    return true;
  }

  public boolean doRefence(String type, String code, String refCode) {
    Criteria cri = Criteria.where("type").is(type).and("code").is(code);
    Update u = new Update();
    u.set("refCode", refCode);
    u.set("createTime", new Date());
    commonRepository.updateOrInsert(cri, u, ContantUtil.DS_REFRENCE_COLLECTION_NAME);
    return true;
  }

  public boolean removeRefence(String type, String code, String refCode) {
    Criteria cri = Criteria.where("type").is(type).and("code").is(code);
    if (!StringUtils.isEmpty(refCode)) {
      cri.and("refCode").is(refCode);
    }
    commonRepository.delData(cri, ContantUtil.DS_REFRENCE_COLLECTION_NAME);
    return true;
  }

  public void canDel(String id, String type) {}

  public List<Map> refList(String type, String refCode) {
    String refName = "";
    if ("数据元".equals(type)) {
      refName = ContantUtil.DS_COLLECTION_NAME;
    } else if ("数据集".equals(type)) {
      refName = ContantUtil.DS_SET_COLLECTION_NAME;
    }
    Criteria cri = Criteria.where("type").is(type).and("refCode").is(refCode);
    List<AggregationOperation> aggs = new ArrayList<>();
    aggs.add(Aggregation.match(cri));

    aggs.add(Aggregation.lookup(refName, "code", "code", "r1"));
    aggs.add(Aggregation.unwind("r1"));
    aggs.add(Aggregation.sort(Sort.Direction.ASC, "createTime"));
    aggs.add(Aggregation.project("r1.code", "r1.name"));
    List<Map> d = commonRepository.agg(aggs, ContantUtil.DS_REFRENCE_COLLECTION_NAME);
    return d.stream()
        .filter(_d -> _d.get("name") != null && _d.get("name").toString().length() > 0)
        .collect(Collectors.toList());
  }

  public long refCount(String type, String refCode) {
    return commonRepository.count(
        Criteria.where("type").is(type).and("refCode").is(refCode),
        ContantUtil.DS_REFRENCE_COLLECTION_NAME);
  }

  public List<Map> refCount(String type, List<String> refCode) {
    Criteria cri = Criteria.where("type").is(type).and("refCode").in(refCode);
    List<AggregationOperation> aggs = new ArrayList<>();
    aggs.add(Aggregation.match(cri));
    aggs.add(Aggregation.group("refCode").count().as("count"));
    return commonRepository.agg(aggs, ContantUtil.DS_REFRENCE_COLLECTION_NAME);
  }
}
