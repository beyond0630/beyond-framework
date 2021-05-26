package org.beyond.longpolling.config;

import java.util.List;

import org.beyond.longpolling.common.*;
import org.beyond.longpolling.common.result.Result;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class LongPollingConfig {

    @Bean(destroyMethod = "shutdown")
    public static PollableMessageBroker<Result<List<Message>>> pollableMessageBroker(
            RedisTemplate<String, Object> redisTemplate) {
        return new DefaultPollableMessageBroker<>(new RedisMessageChannelFactory<>(Result::ok, redisTemplate));
    }

}