package com.demon.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Component
@EnableScheduling
public class RedisOffsetListLocalCacheSynchronizer implements DisposableBean, InitializingBean, LocalCacheSynchronizer {

    @Resource
    private List<LocalCache> localCaches;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private RedisSerializer<String> stringRedisSerializer;

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalCacheSynchronizer.class);

    @Scheduled(fixedDelay = 60 * 1000, initialDelay = 10 * 1000)
    private void synchronize() {
        localCaches.forEach(localCache -> {
            try {
                String cacheId = redisTemplate.execute((RedisCallback<String>) connection -> {
                    byte[] key = stringRedisSerializer.serialize(getMediaKey(localCache.media()));
                    long length = connection.listCommands().lLen(key);
                    int offset = ((Offset)localCache).current();
                    if (length > offset) {
                        return stringRedisSerializer.deserialize(connection.listCommands().lIndex(key, offset));
                    }
                    return null;
                });
                if (StringUtils.hasText(cacheId)) {
                    localCache.remove(cacheId);
                    ((Offset)localCache).offset();
                }
            } catch (Exception e) {
                LOGGER.error("synchronize local cache error", e);
            }
        });
    }

    @Override
    public void publish(String cacheId, Media media) {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] key = serializer.serialize(getMediaKey(media));
            byte[] value = serializer.serialize(cacheId);
            connection.listCommands().rPush(key, value);
            return null;
        });
    }

    private String getMediaKey(Media media) {
        return "mc:sync:" + media.name().toLowerCase();
    }

    @Override
    public void destroy() {
        localCaches.stream()
                .map(LocalCache::media)
                .map(this::getMediaKey)
                .forEach(redisTemplate::delete);
    }

    @Override
    public void afterPropertiesSet() {
        stringRedisSerializer = redisTemplate.getStringSerializer();
    }
}
