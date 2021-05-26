package org.beyond.longpolling.common.redis;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.beyond.longpolling.common.Message;
import org.beyond.longpolling.common.MessageQueue;
import org.beyond.longpolling.common.json.JsonUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

public class RedisMessageQueue implements MessageQueue {

    private final String key;

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisMessageQueue(final String key,
                             final RedisTemplate<String, Object> redisTemplate) {
        this.key = key;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void pushMessage(final Message message) {
        this.redisTemplate.opsForList().rightPush(this.key, message);
        this.redisTemplate.expire(key, 1, TimeUnit.DAYS);
    }

    @Override
    public List<Message> popAllMessages() {
        final Long size = redisTemplate.opsForList().size(key);
        Assert.notNull(size, String.format("key[%s] not exists", key));
        final List<Message> result = redisTemplate.opsForList().range(key, 0, (int) (size - 1))
                .stream()
                .map(x -> JsonUtils.deserialize(JsonUtils.serialize(x), Message.class))
                .collect(Collectors.toList());
        redisTemplate.delete(key);
        return result;
    }
}
