package org.beyond.longpolling.common.redis;

import org.beyond.longpolling.common.ResultConverter;
import org.beyond.longpolling.common.BaseMessageChannel;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisMessageChannel<R> extends BaseMessageChannel<R> {

    public RedisMessageChannel(final String id,
                               final ResultConverter<R> converter,
                               final RedisTemplate<String, Object> redisTemplate) {
        super(id, converter, redisTemplate);
    }

    @Override
    public void close() {

    }
}
