package com.hm.editor.adminservice.infrastructure.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/** Created by taota on 2017/12/12. */
public class ExceptionUtil {

    public static String getStackTraceFirstLine(Exception e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos));
        String stackTraceStr = baos.toString();
        int lineBreakIndex = stackTraceStr.indexOf("\n");
        return stackTraceStr.substring(0, lineBreakIndex);
    }

    public static String getStackTrace(Exception e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos));
        return baos.toString();
    }
}
