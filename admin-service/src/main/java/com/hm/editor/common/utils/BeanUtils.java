package com.hm.editor.common.utils;

import java.util.ArrayList;
import java.util.List;

public class BeanUtils {

    public static <S, T extends S> T parent2child(S source) {
        return source == null ? null : (T) source;
    }

    public static <S, T extends S> List<T> parents2children(List<S> source) {
        if (source == null) return null;
        List<T> target = new ArrayList<>();
        for (S s : source) {
            target.add(parent2child(s));
        }
        return target;
    }
}
