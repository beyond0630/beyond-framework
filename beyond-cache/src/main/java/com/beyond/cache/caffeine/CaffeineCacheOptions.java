package com.beyond.cache.caffeine;

import com.beyond.cache.CacheOptions;

import java.time.Duration;

/**
 * @author beyond
 * @since 2022/12/2
 */
public interface CaffeineCacheOptions extends CacheOptions {

    int initialCapacity();

    Duration getRefreshAfterWrite();

}
