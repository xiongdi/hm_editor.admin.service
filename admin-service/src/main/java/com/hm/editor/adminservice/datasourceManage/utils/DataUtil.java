package com.hm.editor.adminservice.datasourceManage.utils;

import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;

public class DataUtil {
  public static void objectId2Str(List<Map> data, String... keys) {
    for (Map d : data) {
      objectId2Str(d, keys);
    }
  }

  public static void objectId2Str(Map d, String... keys) {
    if (keys == null || keys.length == 0) {
      objectId2StrByKey(d, "_id");
    } else {
      for (String k : keys) {
        objectId2StrByKey(d, k);
      }
    }
  }

  public static void objectId2StrByKey(Map d, String k) {
    if (d != null && d.containsKey(k)) {
      d.put(k, object2Str(d.get(k)));
    }
  }

  private static String object2Str(Object o) {
    return o == null ? "" : o.toString();
  }

  public static ObjectId str2ObjectId(String str) {
    return new ObjectId(str);
  }
}
