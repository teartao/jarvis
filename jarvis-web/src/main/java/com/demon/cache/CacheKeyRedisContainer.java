package com.demon.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class CacheKeyRedisContainer implements InitializingBean {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private RedisSerializer<String> stringRedisSerializer;

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheKeyRedisContainer.class);

    public void push(CacheKey cacheKey, long expire, TimeUnit expireUnit) {
        try {
            String containerKey = getContainerKey(cacheKey.cacheId());
            redisTemplate.execute((RedisCallback<Object>) connection -> {
                byte[] key = stringRedisSerializer.serialize(containerKey);
                byte[] value = stringRedisSerializer.serialize(cacheKey.cacheKey());
                connection.listCommands().rPush(key, value);
                return null;
            });
            redisTemplate.expire(containerKey, expire, expireUnit);
        } catch (Exception e) {
            LOGGER.error("record cache key error", e);
        }
    }

    public List<String> get(String cacheId) {
        String containerKey = getContainerKey(cacheId);
        List<byte[]> bytes = redisTemplate.execute((RedisCallback<List<byte[]>>) connection -> {
            byte[] key = stringRedisSerializer.serialize(containerKey);
            long length = connection.listCommands().lLen(key);
            return connection.listCommands().lRange(key, 0, length);
        });
        if(!CollectionUtils.isEmpty(bytes)){
            return bytes.stream()
                    .map(stringRedisSerializer::deserialize)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public void clear(String cacheId) {
        redisTemplate.delete(getContainerKey(cacheId));
    }

    private String getContainerKey(String cacheId) {
        return Detector.SIGN + cacheId + ":container";
    }

    @Override
    public void afterPropertiesSet() {
        stringRedisSerializer = redisTemplate.getStringSerializer();
    }
}
