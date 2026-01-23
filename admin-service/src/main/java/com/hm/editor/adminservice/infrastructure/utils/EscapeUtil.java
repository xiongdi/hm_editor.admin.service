package com.hm.editor.adminservice.infrastructure.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EscapeUtil {

    /**
     * 转义正则特殊字符 （$()*+.[]?\^{},|）
     *
     * @param keyword
     * @return
     */
    public static String escapeExprSpecialWord(String keyword) {
        if (keyword != null && !keyword.equals("")) {
            String[] fbsArr = {
                "\\",
                "$",
                "(",
                ")",
                "*",
                "+",
                ".",
                "[",
                "]",
                "?",
                "^",
                "{",
                "}",
                "|",
            };
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }

    /**
     * 正则匹配是否包含汉字
     *
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
}
