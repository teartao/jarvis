package com.demon.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CachedRemove {

    String id() default "";

    Media media() default Media.REDIS;

    String spelId() default "";

    String spelMedia() default "";
}
