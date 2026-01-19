package com.hm.editor.common.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

    public static <T> List<List<T>> divideIntoBatch(List<T> list, int oneBatchSize) {
        List<List<T>> result = new ArrayList<>();
        int length = list.size();
        for (int i = 0; i < length; i += oneBatchSize) {
            if (length < i + oneBatchSize) {
                result.add(list.subList(i, length));
            } else {
                result.add(list.subList(i, i + oneBatchSize));
            }
        }
        return result;
    }
}
