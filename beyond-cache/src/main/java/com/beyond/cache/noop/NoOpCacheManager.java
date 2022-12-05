package com.beyond.cache.noop;

import com.beyond.cache.Cache;
import com.beyond.cache.CacheManager;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author beyond
 * @since 2022/12/2
 */
public class NoOpCacheManager implements CacheManager {


    Map<String, NoOpCache> caches = new ConcurrentHashMap<>(64);


    @Override
    public <K, V> Cache<K, V> createCache(final Class<K> keyClz, final Class<V> valClz) {
        return this.createCache(keyClz, valClz, 0, Duration.ofSeconds(0));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Cache<K, V> createCache(final Class<K> keyClz,
                                          final Class<V> valClz,
                                          final int capacity,
                                          final Duration duration) {
        final String key = this.getUnequalKey(keyClz, valClz);
        return Optional.ofNullable(this.caches.get(key))
            .orElseGet(() -> {
                final NoOpCache<?, ?> cache = new NoOpCache<>();
                caches.put(key, cache);
                return cache;
            });

    }


    private String getUnequalKey(final Class<?> keyClz,
                                 final Class<?> valClz) {
        return String.format("%s-%s", keyClz.getName(), valClz.getName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Cache<K, V> getCache(final Class<K> keyClz, final Class<V> valClz) {
        return (Cache<K, V>) caches.get(this.getUnequalKey(keyClz, valClz));
    }


}
