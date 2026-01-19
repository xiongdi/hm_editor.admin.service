package com.hm.editor.common.utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/** 字符串工具类 Created by dingyuanyuan on 2019/1/15. */
public class StringUtils {

    public static ConcurrentHashMap<String, List<String[]>> CACHE_EXPRESS =
        new ConcurrentHashMap<>();

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return null == str || str.trim().isEmpty();
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return null != str && !str.trim().isEmpty();
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return null != str && !str.trim().isEmpty();
    }

    /**
     * 判断字符串为空
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return null == str || str.trim().isEmpty();
    }

    /**
     * 字符串连接
     *
     * @param array
     * @param separator
     * @param startIndex
     * @param endIndex
     * @return
     */
    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        } else {
            if (separator == null) {
                separator = "";
            }

            int bufSize = endIndex - startIndex;
            if (bufSize <= 0) {
                return "";
            } else {
                bufSize *=
                    (array[startIndex] == null ? 16 : array[startIndex].toString().length()) +
                    separator.length();
                StringBuilder buf = new StringBuilder(bufSize);

                for (int i = startIndex; i < endIndex; ++i) {
                    if (i > startIndex) {
                        buf.append(separator);
                    }

                    if (array[i] != null) {
                        buf.append(array[i]);
                    }
                }

                return buf.toString();
            }
        }
    }

    public static boolean orStartWithStr(String value, String orStr) {
        return orStartWithStr(value, orStr, "\\|\\|");
    }

    public static boolean orStartWithStr(String value, String orStr, String splitChar) {
        if (orStr == null) {
            return false;
        }
        String[] orStrArr = orStr.split(splitChar);
        for (String or : orStrArr) {
            boolean flag = value.startsWith(or.trim());
            if (flag) {
                return flag;
            }
        }

        return false;
    }

    public static boolean isNull(Object obj) {
        return (
            obj == null ||
            obj.toString().trim().isEmpty() ||
            (obj instanceof Map && ((Map<?, ?>) obj).isEmpty()) ||
            (obj instanceof Collection && ((Collection<?>) obj).isEmpty())
        );
    }

    /** 左右括号是否匹配 */
    public static boolean isComplete(String s) {
        if (isBlank(s) || ((!s.contains("(")) && !s.contains(")"))) {
            return true;
        }
        char[] chars = s.toCharArray();
        int len = chars.length;

        Stack<Character> left = new Stack<>();
        int pos = 0;
        while (pos < len) {
            char cur = chars[pos];
            if (cur == '(') {
                left.push(cur);
            } else if (cur == ')') {
                if (left.isEmpty()) {
                    return false;
                }
                left.pop();
            }
            pos++;
        }
        return true;
    }

    public static boolean equals(String s1, String s2) {
        if (s1 == s2) {
            return true;
        }
        if (s1 == null || s2 == null) {
            return false;
        }
        return s1.equals(s2);
    }

    public static boolean webParamStrEmpty(String str) {
        return isBlank(str) || "null".equals(str) || "undefined".equals(str);
    }

    public static String trim(String str) {
        if (str != null) {
            // 去除字符串前后全角空格
            String regLeft = "^[\\u3000]|[\\u00A0]*|[\\s]";
            String regRight = "[\\u3000]|[\\u00A0]|[\\s]*$";
            str = str.replaceAll(regLeft, "").replaceAll(regRight, "").trim();
            return str;
        } else {
            return null;
        }
    }

    /** 数据脱敏 express : ${长度} *{长度} */
    public static Object scrypt(Object valObj, String express) {
        if (valObj == null) {
            return valObj;
        }
        if (!(valObj instanceof String)) {
            return valObj;
        }
        String val = valObj.toString();

        if (isBlank(val)) {
            return val;
        }
        if (isBlank(express)) {
            return val;
        }

        List<String[]> expression = parseExpres(express);
        if (expression == null || expression.isEmpty()) {
            return val;
        }
        // n配置不能超过一个
        int nCount = 0;

        String[] nbak = null;

        String s1 = "";
        String s2 = "";
        String s3 = "";

        int left = 0;
        int right = val.length();
        int expLen = expression.size();

        int nIndex = -1;
        for (int i = 0; i < expLen; i++) {
            String[] exp = expression.get(i);
            String s = exp[0];
            String len = exp[1];
            if ("n".equals(len) && nCount > 0) {
                // 超过多个n配置，无法脱敏
                return val;
            }
            if ("n".equals(len)) {
                // 从后往前解析
                nbak = exp;
                nCount++;
                nIndex = i;
                break;
            } else {
                try {
                    int lenNum = Integer.parseInt(len);
                    if (left >= right) {
                        break;
                    }
                    if (left + lenNum > right) {
                        lenNum = right - left;
                    }
                    if ("$".equals(s)) {
                        s1 += substr(val, left, lenNum);
                    } else {
                        for (int sl = 0; sl < lenNum; sl++) {
                            s1 += "*";
                        }
                    }
                    left += lenNum;
                } catch (Exception e) {
                    LogUtils.error("scrypt {} error:{}", express, e);
                    return val;
                }
            }
        }

        if (nIndex > -1) {
            for (int i = expLen - 1; i > nIndex; i--) {
                String[] exp = expression.get(i);
                String s = exp[0];
                String len = exp[1];
                if ("n".equals(len)) {
                    // 超过多个n配置，无法脱敏
                    return val;
                }
                try {
                    int lenNum = Integer.parseInt(len);
                    if (left >= right) {
                        break;
                    }
                    if (left + lenNum > right) {
                        lenNum = right - left;
                    }
                    if ("$".equals(s)) {
                        s3 = substr(val, right - lenNum, lenNum) + s3;
                    } else {
                        for (int sl = 0; sl < lenNum; sl++) {
                            s3 = "*" + s3;
                        }
                    }
                    right = right - lenNum;
                } catch (Exception e) {
                    LogUtils.error("scrypt {} error:{}", express, e);
                    return val;
                }
            }

            if (left < right) {
                int len = right - left;
                if ("$".equals(nbak[0])) {
                    s2 = substr(val, left, len);
                } else {
                    for (int sl = 0; sl < len; sl++) {
                        s2 += "*";
                    }
                }
            }
        }
        return s1 + s2 + s3;
    }

    private static List<String[]> parseExpres(String express) {
        List<String[]> expression = CACHE_EXPRESS.get(express);
        if (expression != null) {
            return expression;
        }
        expression = new ArrayList<>();

        int length = express.length();
        for (int i = 0; i < length; i++) {
            char cur = express.charAt(i);
            if (cur != '$' && cur != '*') {
                continue;
            }
            try {
                String[] oneExp = parseOneExp(express, i);
                expression.add(oneExp);
            } catch (RuntimeException e) {
                LogUtils.error("parseExpres {} error", express);
                expression.clear();
            }
        }
        CACHE_EXPRESS.put(express, expression);
        return expression;
    }

    private static String[] parseOneExp(String exp, int start) throws RuntimeException {
        String msg = "express:" + exp + " @" + start + " error";
        String[] s = new String[2];
        s[0] = exp.charAt(start) + "";
        int length = exp.length();
        int next = start + 1;
        if (next < length && exp.charAt(next) == '{') {
            next++;
            String len = "";
            for (; next < length; next++) {
                if (exp.charAt(next) == '}') {
                    break;
                }
                len += exp.charAt(next);
            }
            if ("n".equals(len) || len.replaceAll("\\d+", "").trim().isEmpty()) {
                s[1] = len;
            } else {
                throw new RuntimeException(msg);
            }
        } else {
            throw new RuntimeException(msg);
        }
        return s;
    }

    private static String substr(String str, int start, int length) {
        if (isBlank(str)) {
            return "";
        }
        if (start < 0) {
            start = 0;
        }
        int len = str.length();
        if (len <= start) {
            return "";
        }
        if (len < start + length) {
            return str.substring(start, len);
        } else {
            return str.substring(start, start + length);
        }
    }
}
