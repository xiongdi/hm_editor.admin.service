package com.hm.editor.adminservice.console.utils;

import java.util.UUID;

/**
 * @PROJECT_NAME:service
 * @author:wanglei
 * @date:2020/11/11 10:10 AM
 * @Description:
 */
public class FolderUtils {
    public static String uuid(int len){
        if(len < 0) {
            return "";
        }
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        if(len > uuid.length()){
            return uuid;
        }
        return uuid.substring(0,len);
    }
}
