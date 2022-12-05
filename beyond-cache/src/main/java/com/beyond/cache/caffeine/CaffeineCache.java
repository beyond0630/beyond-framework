package com.beyond.cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;

import java.util.function.Supplier;

/**
 * @author beyond
 * @email
 * @since 2022/12/1
 */
public class CaffeineCache<K, V> implements com.beyond.cache.Cache<K, V> {

    private final Cache<K, V> cache;

    public CaffeineCache(final Cache<K, V> caffeine) {
        this.cache = caffeine;
    }


    @Override
    public V get(final K k) {
        return cache.getIfPresent(k);
    }

    @Override
    public void put(final K k, final V v) {
        cache.put(k, v);
    }

    @Override
    public V evict(final K k) {
        final V v = this.get(k);
        cache.invalidate(k);
        return v;
    }

    @Override
    public V getOrDefault(final K k, final V v) {
        final V value = this.get(k);
        return value == null ? v : value;
    }

    @Override
    public V getIfAbsent(final K k, final Supplier<V> supplier) {
        V value = this.get(k);
        if (value == null) {
            value = supplier.get();
            this.put(k, value);
        }
        return value;
    }

    @Override
    public void clear() {
        cache.invalidateAll();
    }
}
