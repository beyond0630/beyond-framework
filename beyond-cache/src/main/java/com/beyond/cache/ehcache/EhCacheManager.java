package com.beyond.cache.ehcache;

import com.beyond.cache.Cache;
import com.beyond.cache.CacheManager;
import com.beyond.cache.CacheOptions;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import java.time.Duration;

/**
 * @author beyond
 * @since 2022/12/2
 */
public class EhCacheManager implements CacheManager {

    private final CacheOptions cacheOptions;
    private final org.ehcache.CacheManager manager;

    public EhCacheManager(final CacheOptions cacheOptions) {
        this.cacheOptions = cacheOptions;
        this.manager = CacheManagerBuilder.newCacheManagerBuilder().build(true);;
    }

    @Override
    public <K, V> Cache<K, V> createCache(final Class<K> keyClz, final Class<V> valClz) {
        return this.createCache(keyClz, valClz, cacheOptions.getMaxCapacity(), cacheOptions.getExpiration());
    }

    @Override
    public <K, V> Cache<K, V> createCache(final Class<K> keyClz,
                                          final Class<V> valClz,
                                          final int maxCapacity,
                                          final Duration duration) {
        final org.ehcache.Cache<K, V> cache = manager.createCache(this.getUnequalKey(keyClz, valClz),
            CacheConfigurationBuilder.newCacheConfigurationBuilder(keyClz, valClz, ResourcePoolsBuilder.heap(maxCapacity))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(duration)).build());
        return new EhCache<>(cache);
    }

    @Override
    public <K, V> Cache<K, V> getCache(final Class<K> keyClz, final Class<V> valClz) {
        return this.createCache(keyClz, valClz, cacheOptions.getMaxCapacity(), cacheOptions.getExpiration());
    }

    private String getUnequalKey(final Class<?> keyClz,
                                 final Class<?> valClz) {
        return String.format("%s-%s", keyClz.getName(), valClz.getName());
    }
}
