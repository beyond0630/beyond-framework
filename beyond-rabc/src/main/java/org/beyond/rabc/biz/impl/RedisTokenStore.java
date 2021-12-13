package org.beyond.rabc.biz.impl;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

import io.jsonwebtoken.lang.Assert;
import org.beyond.rabc.biz.TokenStore;
import org.beyond.rabc.option.TokenOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author Beyond
 */
@Component
public class RedisTokenStore implements TokenStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisTokenStore.class);

    private final TokenOptions tokenOptions;
    private final StringRedisTemplate redisTemplate;


    public RedisTokenStore(final TokenOptions tokenOptions, final StringRedisTemplate redisTemplate) {
        this.tokenOptions = tokenOptions;
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void check() {
        String issuer = tokenOptions.getIssuer();
        if (StringUtils.isEmpty(issuer)) {
            LOGGER.warn("please set token issuer");
        }
    }

    @Override
    public boolean checkToken(final long userId, final String token) {
        Assert.hasText(token, "token can not be null");
        String key = this.formatKey(userId);
        String storeToken = redisTemplate.opsForValue().get(key);
        return token.equals(storeToken);
    }

    @Override
    public void storeToken(final long userId, final String token) {
        String key = this.formatKey(userId);
        redisTemplate.opsForValue().set(key, token, this.tokenOptions.getDuration(), TimeUnit.SECONDS);
    }

    @Override
    public void removeToken(final long userId) {
        String key = this.formatKey(userId);
        redisTemplate.delete(key);
    }

    private String formatKey(final long userId) {
        return String.format("%s:%s:%s:%s",
            Optional.ofNullable(tokenOptions.getIssuer()).orElse("beyond-rabc"),
            Optional.ofNullable(tokenOptions.getEnv()).orElse("default"),
            "token", userId);
    }

}
