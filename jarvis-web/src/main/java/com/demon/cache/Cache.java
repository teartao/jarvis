package com.demon.cache;

import com.demon.bean.MatchingBean;

import java.util.List;

/**
 * 缓存
 *
 * @param <K> 键
 * @param <V> 值
 * @author 杨洋17
 */
public interface Cache<K, V> extends MatchingBean<Media> {

    /**
     * 向缓存中写值
     *
     * @param key       键
     * @param value     值
     * @param condition 附加条件
     */
    void put(K key, V value, Condition condition);

    /**
     * 从缓存中读取值
     *
     * @param key 键
     * @return 值
     */
    V get(K key);

    /**
     * 从缓存中批量读取
     *
     * @param keys 键集合
     * @return 值
     */
    List<V> mGet(List<K> keys);

    /**
     * 从缓存中移除值
     *
     * @param key 键
     */
    void remove(K key);

    /**
     * 从缓存中批量移除
     *
     * @param keys 键集合
     */
    void remove(List<K> keys);

    /**
     * key是否需要存储
     */
    default boolean keyStore() {
        return false;
    }
}
