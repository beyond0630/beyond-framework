package com.beyond.cache.caffeine;

import com.beyond.cache.Cache;
import com.beyond.cache.CacheManager;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author beyond
 * @since 2022/12/2
 */
public class CaffeineCacheManger implements CacheManager {

    private final CaffeineCacheOptions cacheOptions;

    Map<String, CacheHolder> caches = new ConcurrentHashMap<>(64);

    public CaffeineCacheManger(final CaffeineCacheOptions cacheOptions) {
        this.cacheOptions = cacheOptions;
    }

    @Override
    public <K, V> Cache<K, V> createCache(final Class<K> keyClz, final Class<V> valClz) {
        return this.createCache(keyClz, valClz, cacheOptions.getMaxCapacity(), cacheOptions.getExpiration());
    }

    @Override
    public <K, V> Cache<K, V> createCache(final Class<K> keyClz,
                                          final Class<V> valClz,
                                          final int capacity,
                                          final Duration duration) {
        return Optional.ofNullable(this.getCache(keyClz, valClz))
            .orElseGet(() -> {
                synchronized (this.getUnequalKey(keyClz, valClz).intern()) {
                    return Optional.ofNullable(this.getCache(keyClz, valClz))
                        .orElseGet(() -> {
                            final Cache<K, V> cache = this.createCacheCore(capacity, duration);
                            caches.put(this.getUnequalKey(keyClz, valClz), new CacheHolder(keyClz, valClz, cache));
                            return cache;
                        });
                }
            });
    }

    private <K, V> Cache<K, V> createCacheCore(final int capacity,
                                               final Duration duration) {
        final com.github.benmanes.caffeine.cache.Cache<K, V> cache =
            Caffeine.newBuilder()
                .initialCapacity(cacheOptions.getMaxCapacity())
                .maximumSize(capacity)
                .expireAfterAccess(duration)
                .recordStats()
                .build();
        return new CaffeineCache<>(cache);
    }

    private String getUnequalKey(final Class<?> keyClz,
                                 final Class<?> valClz) {
        return String.format("%s-%s", keyClz.getName(), valClz.getName());
    }

    @Override
    public <K, V> Cache<K, V> getCache(final Class<K> keyClz, final Class<V> valClz) {
        return Optional.ofNullable(caches.get(this.getUnequalKey(keyClz, valClz)))
            .map(x -> x.getCache(keyClz, valClz))
            .orElse(null);
    }

    private static class CacheHolder {

        private final Class<?> keyType;
        private final Class<?> valueType;
        private final Cache<?, ?> cache;

        private CacheHolder(final Class<?> keyType, final Class<?> valueType,
                            final Cache<?, ?> cache) {
            this.keyType = keyType;
            this.valueType = valueType;
            this.cache = cache;
        }

        @SuppressWarnings("unchecked")
        <K, V> Cache<K, V> getCache(Class<K> keyClz, Class<V> valClz) {
            if (keyType == keyClz && valueType == valClz) {
                return (Cache<K, V>) cache;
            } else {
                throw new IllegalArgumentException();
            }
        }
    }
}
