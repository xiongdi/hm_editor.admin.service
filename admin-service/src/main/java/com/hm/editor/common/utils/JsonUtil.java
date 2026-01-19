package com.hm.editor.common.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.util.ArrayList;
import java.util.List;

/** Created by taota on 2017/7/26. */
public class JsonUtil {

    public static String toJSONString(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LogUtils.error("toJSONString error", e);
        }
        return null;
    }

    public static <T> T parseObject(String str, Class<T> cls) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        try {
            return mapper.readValue(str, cls);
        } catch (Exception e) {
            LogUtils.error("parseObject error, str: {}", str, e);
        }
        return null;
    }

    public static <T> List<T> parseArray(String str, Class<T> cls) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        CollectionType listType = mapper
            .getTypeFactory()
            .constructCollectionType(ArrayList.class, cls);
        try {
            return mapper.readValue(str, listType);
        } catch (Exception e) {
            LogUtils.error("parseArray error, str: {}", str, e);
        }
        return null;
    }

    public static <T> T parseObjectFromFile(String filePath, Class<T> cls) {
        String txt = FileUtil.txtToString(filePath);
        return parseObject(txt, cls);
    }

    public static <T> List<T> parseArrayFromFile(String filePath, Class<T> cls) {
        String txt = FileUtil.txtToString(filePath);
        return parseArray(txt, cls);
    }

    public static <T> T parseObjectFromJarFile(Class<?> receiverCls, String filePath, Class<T> cls) {
        String txt = FileUtil.jarTxtToString(receiverCls, filePath);
        return parseObject(txt, cls);
    }

    public static <T> T parseObjectFromAbsoluteFile(
        Class<?> receiverCls,
        String filePath,
        Class<T> cls
    ) {
        String txt = FileUtil.absoluteTxtToString(receiverCls, filePath);
        return parseObject(txt, cls);
    }

    // 处理体温单配置中存在的多余转义符
    public static String handleErroJson(String jsonStr) {
        if (StringUtils.isEmpty(jsonStr)) {
            return jsonStr;
        }
        // 去除匹配到的双引号前所有转义符、将匹配所有非双引号前转义符换为2个
        return jsonStr.replaceAll("\\\\+(?=\")", "").replaceAll("\\\\+(?!\"|\\\\+)", "\\\\\\\\");
    }
}
