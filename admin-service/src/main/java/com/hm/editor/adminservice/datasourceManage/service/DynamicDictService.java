package com.hm.editor.adminservice.datasourceManage.service;

import com.hm.editor.adminservice.datasourceManage.repository.CommonRepository;
import com.hm.editor.adminservice.datasourceManage.repository.DynamicDictRepository;
import com.hm.editor.adminservice.datasourceManage.utils.ContantUtil;
import com.hm.editor.adminservice.datasourceManage.utils.DataUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/** 动态值域服务 */
@Service
public class DynamicDictService {
  private String dynamicDictColName = ContantUtil.DYNAMIC_DICT_COLLECTION_NAME;

  @Autowired CommonService commonService;

  @Autowired CommonRepository commonRepository;

  @Autowired DynamicDictRepository dynamicDictRepository;

  @Autowired private MongoTemplate mongoTemplate;

  // 动态值域新增、编辑
  public boolean editorDynamicDict(Map<String, Object> data) throws Exception {
    String idKey = "_id";

    // 如果没有_id字段，表示是新增操作，生成编码
    if (!data.containsKey(idKey)) {
      String newCode = generateNewDictCode();
      data.put("code", newCode);
      // 新增时设置删除标识为0（未删除）
      data.put("isDeleted", 0);
    }

    if (data.containsKey(idKey)) {
      data.put(idKey, DataUtil.str2ObjectId(data.get(idKey).toString()));
    }
    data.put("createTime", new Date());
    data.put("updateTime", new Date());

    return commonService.checkAndSave(
        data,
        dynamicDictColName,
        "code",
        "code",
        "name",
        "tag",
        "url",
        "paramExample",
        "returnCode",
        "returnName",
        "returnExt1",
        "returnExt2",
        "displayContent",
        "createTime",
        "updateTime",
        "isDeleted");
  }

  // 删除动态值域（逻辑删除，只标记isDeleted=1）
  public boolean delDynamicDict(String id) {
    Query query = new Query(Criteria.where("_id").is(DataUtil.str2ObjectId(id)));
    Update update = new Update().set("isDeleted", 1).set("updateTime", new Date());
    return mongoTemplate.updateFirst(query, update, dynamicDictColName).getModifiedCount() > 0;
  }

  // 获取动态值域列表（分页）
  public Map getDynamicDict(String text, int pageNo, int pageSize) {
    // 创建 Criteria 对象，用于构建查询条件
    Criteria criteria = new Criteria();

    // 添加未删除条件（isDeleted为0或不存在）
    criteria.orOperator(
        Criteria.where("isDeleted").is(0), Criteria.where("isDeleted").exists(false));

    // 使用 $and 操作符组合未删除条件和搜索条件
    Criteria searchCriteria = new Criteria();
    searchCriteria.orOperator(
        Criteria.where("code").regex(text, "i"), // 'i' 表示忽略大小写
        Criteria.where("name").regex(text, "i"),
        Criteria.where("tag").regex(text, "i"));

    criteria.andOperator(searchCriteria);

    // 调用 commonService.pageData 方法，并传入构建好的查询条件
    Map d = commonService.pageData(dynamicDictColName, criteria, pageNo, pageSize, "code");
    List<Map> data = (List) d.get("data");

    data.forEach(
        _d -> {
          DataUtil.objectId2Str(_d);
        });

    return d;
  }

  // 获取所有动态值域（只返回未删除的）
  public List<Map> allDynamicDict() {
    Criteria criteria = new Criteria();
    criteria.orOperator(
        Criteria.where("isDeleted").is(0), Criteria.where("isDeleted").exists(false));

    List<Map> d = commonRepository.find(dynamicDictColName, criteria, Sort.by("code"));
    DataUtil.objectId2Str(d);
    return d;
  }

  // 根据编码获取动态值域（只返回未删除的）
  public Map getDynamicDictByCode(String code) {
    Criteria criteria = Criteria.where("code").is(code);
    criteria.andOperator(
        new Criteria()
            .orOperator(
                Criteria.where("isDeleted").is(0), Criteria.where("isDeleted").exists(false)));

    Map d = commonRepository.findOne(dynamicDictColName, criteria);
    if (d != null) {
      DataUtil.objectId2Str(d);
    }
    return d;
  }

  // 根据编码获取动态值域实体（只返回未删除的）
  public List<Map> findByCode(String code) {
    return dynamicDictRepository.findByCode(code);
  }

  /**
   * 生成新的动态字典编码 格式为DCxx.xxx, 初始值为DC00.001 从001开始递增，DC00.999的下一个值为DC01.000
   * DC01.999的下一个值为DC02.000，最大值为DC99.999
   *
   * @return 新的编码
   * @throws Exception 当达到最大值或解析出错时抛出异常
   */
  private String generateNewDictCode() throws Exception {
    // 查询所有以 DC 开头的值域编码并按数字部分排序（包括已删除的）
    List<Map> dicts =
        commonRepository.find(
            dynamicDictColName,
            Criteria.where("code").regex("^DC\\d+\\.\\d+"),
            Sort.by(Sort.Direction.DESC, "code"));

    // 默认初始编码
    int majorNum = 0; // 主编码部分 (xx)
    int minorNum = 0; // 次编码部分 (xxx)

    if (!dicts.isEmpty()) {
      String lastCode = (String) dicts.get(0).get("code");
      try {
        // 解析格式为 DCxx.xxx 的编码
        if (lastCode.length() >= 8 && lastCode.startsWith("DC") && lastCode.charAt(4) == '.') {
          String majorPart = lastCode.substring(2, 4);
          String minorPart = lastCode.substring(5, 8);

          majorNum = Integer.parseInt(majorPart);
          minorNum = Integer.parseInt(minorPart);

          // 增加次编码，如果超过999则进位
          minorNum++;
          if (minorNum > 999) {
            majorNum++;
            minorNum = 0;
          }

          // 检查是否超出最大值DC99.999
          if (majorNum > 99) {
            throw new Exception("值域编码已达到最大值DC99.999，无法继续生成");
          }
        }
      } catch (NumberFormatException e) {
        // 如果解析出错，使用默认初始值
        majorNum = 0;
        minorNum = 1;
      }
    } else {
      // 没有记录时，使用初始值DC00.001
      majorNum = 0;
      minorNum = 1;
    }

    // 格式化返回值：DCxx.xxx
    return String.format("DC%02d.%03d", majorNum, minorNum);
  }
}
