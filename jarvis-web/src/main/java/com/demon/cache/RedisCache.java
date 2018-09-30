package com.demon.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 基于redis缓存实现
 *
 * @param <V>
 */
@Component
public class RedisCache<V> implements Cache<CacheKey, V> {

    @Resource
    private RedisTemplate<String, V> redisTemplate;

    @Override
    public void put(CacheKey cacheKey, V value, Condition condition) {
        if (condition.expire() <= 0) {
            throw new IllegalArgumentException("expire需要大于0");
        }
        redisTemplate.opsForValue().set(cacheKey.cacheKey(), value, condition.expire(), condition.expireUnit());
    }

    @Override
    public V get(CacheKey cacheKey) {
        return redisTemplate.opsForValue().get(cacheKey.cacheKey());
    }

    @Override
    public List<V> mGet(List<CacheKey> keys) {
        return redisTemplate.opsForValue().multiGet(keys.stream().map(CacheKey::cacheKey)
                .collect(Collectors.toList()))
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void remove(CacheKey cacheKey) {
        if (StringUtils.hasText(cacheKey.cacheKey())) {
            redisTemplate.delete(cacheKey.cacheKey());
        }
    }

    @Override
    public void remove(List<CacheKey> cacheKeys) {
        redisTemplate.delete(cacheKeys
                .stream()
                .map(CacheKey::cacheKey)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList()));
    }

    @Override
    public boolean keyStore() {
        return true;
    }

    @Override
    public boolean matching(Media media) {
        return media.equals(Media.REDIS);
    }
}
