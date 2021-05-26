package org.beyond.longpolling.common.redis;

import org.beyond.longpolling.common.json.JsonUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class JsonRedisSerializer<T> implements RedisSerializer<T> {

    private final Class<T> clazz;

    public JsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(final T t) throws SerializationException {
        return JsonUtils.serializeAsBytes(t);
    }

    @Override
    public T deserialize(final byte[] bytes) throws SerializationException {
        return JsonUtils.deserialize(bytes, clazz);
    }
}
