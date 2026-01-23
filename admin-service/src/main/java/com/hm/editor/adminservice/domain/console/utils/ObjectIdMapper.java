package com.hm.editor.adminservice.domain.console.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @PROJECT_NAME:service_spce
 *
 * @author:wanglei
 * @date:2021/1/21 1:54 PM @Description:${DESC}
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ObjectIdMapper {
    String name() default "";
}

