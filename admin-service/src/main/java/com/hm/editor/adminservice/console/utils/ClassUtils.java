package com.hm.editor.adminservice.console.utils;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @PROJECT_NAME:service_spce
 * @author:wanglei
 * @date:2021/1/20 1:54 PM
 * @Description:${DESC}
 */
public class ClassUtils {
    private static Logger LOG = LoggerFactory.getLogger(ClassUtils.class);
    /**
     * 获取字段名称
     * @param c
     * @return
     */
    public static String[] fieldArr(Class c,String ...filterNames){
        if(c == null){
            return null;
        }
        Map<String,Boolean> filterNameMap = new HashMap<>();
        for(String n:filterNames){
            filterNameMap.put(n,true);
        }
        Field[] fields = c.getDeclaredFields();
        int len = fields.length;
        String res[] = new String[len];
        int count = 0;
        while(--len > -1){
            String name = fields[len].getName();
            if(!filterNameMap.containsKey(name)){
                res[len] = name;
                count++;
            }
        }
        return Arrays.copyOf(res,count);
    }
    public static List<String> fieldList(Class c,String ...filterNames){
        String[] fields = fieldArr(c,filterNames);
        if(fields == null) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(fields));
    }
    public static void transferObjectId(Object obj){
        if(obj == null) return;
        Field[] fields = obj.getClass().getDeclaredFields();
        int len = fields.length;
        while(--len > -1){
            Field field = fields[len];
            if(field.isAnnotationPresent(ObjectIdMapper.class)){
                Object val = getVal(obj,field.getName());
                if(val != null){
                    if(val instanceof String){
                        // 后台转前端传的值
                        setVal(obj,field.getName(),val.toString().length() == 0?null:new ObjectId(val.toString()));
                    }
                    if(val instanceof ObjectId){
                        // 传给前端
                        setVal(obj,field.getName(),((ObjectId) val).toHexString());
                    }
                }
            }
        }
    }
    public static Object getVal(Object obj,String name) {
        try {
            Field f = obj.getClass().getDeclaredField(name);
            f.setAccessible(true);
            return f.get(obj);
        }catch (Exception e){
            LOG.error("getVal name: {} error:{}",name,e);
            return null;
        }
    }
    public static void setVal(Object obj,String name,Object val){
        try {
            Field f = obj.getClass().getDeclaredField(name);
            f.setAccessible(true);
            f.set(obj,val);
        }catch (Exception e){

            LOG.error("setVal name: {} val:{},error:{}",name,val,e);
        }
    }
}
