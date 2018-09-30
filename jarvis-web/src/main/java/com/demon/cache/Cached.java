package com.demon.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cached {

    /**
     * 缓存ID，唯一标志
     */
    String id();

    /**
     * 缓存的存储媒介
     */
    Media media() default Media.REDIS;

    /**
     * 缓存超时时间Redis，Guava有效
     */
    long expire() default -1;

    /**
     * 超时时间单位
     */
    TimeUnit expireUnit() default TimeUnit.HOURS;

    /**
     * guava cache 有效
     */
    int maximumSize() default 0;

}
