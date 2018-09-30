package com.demon.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * 基于Map的缓存实现
 *
 * @param <V>
 * @author 杨洋17
 */
@Component
public class MapCache<V> implements Cache<CacheKey, V>, LocalCache, Offset {

    private final Map<String, Map<String, V>> group = new HashMap<>();

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private Lock rl = lock.readLock();
    private Lock wl = lock.writeLock();

    private int offset = 0;

    private Media media = Media.MAP;

    private LocalCacheSynchronizer localCacheSynchronizer;

    @Override
    public void put(CacheKey cacheKey, V value, Condition condition) {
        try {
            wl.lock();
            group.computeIfAbsent(cacheKey.cacheId(), k -> new HashMap<>())
                    .put(cacheKey.cacheKey(), value);
        } finally {
            wl.unlock();
        }
    }

    @Override
    public V get(CacheKey cacheKey) {
        try {
            rl.lock();
            Map<String, V> cache = group.get(cacheKey.cacheId());
            if (cache == null) {
                return null;
            }
            return cache.get(cacheKey.cacheKey());
        } finally {
            rl.unlock();
        }
    }

    @Override
    public List<V> mGet(List<CacheKey> cacheKeys) {
        return cacheKeys.stream().map(this::get).collect(Collectors.toList());
    }

    @Override
    public void remove(CacheKey cacheKey) {
        localCacheSynchronizer.publish(cacheKey.cacheId(), Media.MAP);
    }

    @Override
    public void remove(List<CacheKey> cacheKeys) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(String cacheId) {
        Map<String, V> cache = group.get(cacheId);
        if (cache != null) {
            cache.clear();
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
    public boolean matching(Media media) {
        return media.equals(this.media);
    }

    @Autowired
    public void setLocalCacheSynchronizer(LocalCacheSynchronizer localCacheSynchronizer) {
        this.localCacheSynchronizer = localCacheSynchronizer;
    }
}
