package com.demon.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * 基于Guava缓存实现
 *
 * @param <V>
 * @author 杨洋17
 */
@Component
public class GuavaCache<V> implements com.demon.cache.Cache<CacheKey, V>, LocalCache, Offset {

    private Map<String, Cache<String, V>> group = new HashMap<>();

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private Lock rl = lock.readLock();
    private Lock wl = lock.writeLock();

    private Integer defaultMaxSize = 100;

    private int offset = 0;

    private Media media = Media.GUAVA;

    private LocalCacheSynchronizer localCacheSynchronizer;

    @Override
    public void put(CacheKey cacheKey, V value, Condition condition) {
        if (condition.expire() <= 0) {
            throw new IllegalArgumentException("expire需要大于0");
        }
        try {
            wl.lock();
            Cache<String, V> cache = group.get(cacheKey.cacheId());
            if (cache == null) {
                cache = CacheBuilder
                        .newBuilder()
                        .expireAfterWrite(condition.expire(), condition.expireUnit())
                        .maximumSize(condition.maximumSize() <= 0 ? defaultMaxSize : condition.maximumSize())
                        .build();
                group.put(cacheKey.cacheId(), cache);
            }
            cache.put(cacheKey.cacheKey(), value);
        } finally {
            wl.unlock();
        }
    }

    @Override
    public V get(CacheKey cacheKey) {
        try {
            rl.lock();
            Cache<String, V> cache = group.get(cacheKey.cacheId());
            if (cache == null) {
                return null;
            }
            return cache.getIfPresent(cacheKey.cacheKey());
        } finally {
            rl.unlock();
        }
    }

    @Override
    public List<V> mGet(List<CacheKey> cacheKeys) {
        return cacheKeys.stream().map(this::get).collect(Collectors.toList());
    }

    @Override
    public final void remove(CacheKey cacheKey) {
        localCacheSynchronizer.publish(cacheKey.cacheId(), Media.GUAVA);
    }

    @Override
    public final void remove(List<CacheKey> cacheKeys) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(String cacheId) {
        Cache<String, V> cache = group.get(cacheId);
        if (cache != null) {
            cache.invalidateAll();
            group.remove(cacheId);
        }
    }

    @Override
    public int current() {
        return offset;
    }

    @Override
    public void offset() {
        offset++;
    }

    @Override
    public Media media() {
        return media;
    }

    @Override
    public final boolean matching(Media media) {
        return media.equals(this.media);
    }

    @Autowired
    public void setLocalCacheSynchronizer(LocalCacheSynchronizer localCacheSynchronizer) {
        this.localCacheSynchronizer = localCacheSynchronizer;
    }
}
