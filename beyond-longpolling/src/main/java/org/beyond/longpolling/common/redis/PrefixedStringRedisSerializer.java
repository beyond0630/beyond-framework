package org.beyond.longpolling.common.redis;

import java.nio.charset.StandardCharsets;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class PrefixedStringRedisSerializer implements RedisSerializer<String> {

    private static final String DEFAULT_PREFIX = "beyond::framework::longpolling::";
    private final String prefix;

    public PrefixedStringRedisSerializer() {
        this(DEFAULT_PREFIX);
    }

    public PrefixedStringRedisSerializer(final String prefix) {
        this.prefix = prefix;
    }

    @Override
    public byte[] serialize(final String s) throws SerializationException {
        String value = this.prefix + s;
        return value.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String deserialize(final byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }
        String value = new String(bytes, StandardCharsets.UTF_8);
        if (value.startsWith(this.prefix)) {
            return value.replace(this.prefix, "");
        }
        return value;
    }
}
