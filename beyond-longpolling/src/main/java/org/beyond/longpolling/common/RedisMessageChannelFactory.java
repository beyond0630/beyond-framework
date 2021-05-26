package org.beyond.longpolling.common;

import org.beyond.longpolling.common.redis.RedisMessageChannel;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisMessageChannelFactory<R> implements MessageChannelFactory<R> {

    private final ResultConverter<R> converter;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisMessageChannelFactory(final ResultConverter<R> converter,
                                      final RedisTemplate<String, Object> redisTemplate) {
        this.converter = converter;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public MessageChannel<R> newChannel(final String channelId) {
        return new RedisMessageChannel<>(channelId, this.converter, this.redisTemplate);
    }

}