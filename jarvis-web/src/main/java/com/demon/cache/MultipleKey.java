package com.demon.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MultipleKey标注的参数必须为List，而且该方法返回值必须为List
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MultipleKey {

    String name() default "";

    Class<? extends KeyMapper> value() default DefaultKeyMapper.class;
}
