package com.demon.cache;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

//@Configuration
public class RedisPubSubLocalCacheSynchronizer implements LocalCacheSynchronizer, MessageListener, InitializingBean {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private List<LocalCache> localCaches;

    private RedisSerializer<String> stringRedisSerializer;

    @Override
    public void publish(String cacheId, Media media) {
        redisTemplate.convertAndSend(media.name(), cacheId);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String cacheId = stringRedisSerializer.deserialize(message.getBody());
        Media media = Media.of(stringRedisSerializer.deserialize(message.getChannel()));
        localCaches.stream()
                .filter(localCache -> localCache.media().equals(media))
                .findFirst()
                .ifPresent(localCache -> localCache.remove(cacheId));
    }

    @Bean
    public MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(new RedisPubSubLocalCacheSynchronizer());
    }

    @Bean(destroyMethod = "destroy")
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisTemplate.getConnectionFactory());
        redisMessageListenerContainer.addMessageListener(messageListener(),
                localCaches
                        .stream()
                        .map(localCache -> new ChannelTopic(localCache.media().name()))
                        .collect(Collectors.toList()));
        redisMessageListenerContainer.setTaskExecutor(redisPublishThreadPoolScheduler());
        return redisMessageListenerContainer;
    }

    @Bean
    public ThreadPoolTaskScheduler redisPublishThreadPoolScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(3);
        return threadPoolTaskScheduler;
    }

    @Override
    public void afterPropertiesSet() {
        stringRedisSerializer = redisTemplate.getStringSerializer();
    }
}
