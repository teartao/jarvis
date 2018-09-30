package com.demon.cache;

/**
 * @author 杨洋17
 * @param <T> 须重写hashCode和equals方法
 */
public interface ArgumentMatcher<T>{

    T get();
}
