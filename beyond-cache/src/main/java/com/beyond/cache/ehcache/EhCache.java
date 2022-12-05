package com.beyond.cache.ehcache;

import com.beyond.cache.Cache;

import java.util.function.Supplier;

/**
 * @author beyond
 * @since 2022/12/2
 */
public class EhCache<K, V> implements Cache<K, V> {

    private final org.ehcache.Cache<K, V> cache;

    public EhCache(final org.ehcache.Cache<K, V> cache) {
        this.cache = cache;
    }

    @Override
    public V get(final K k) {
        return cache.get(k);
    }

    @Override
    public void put(final K k, final V v) {
        cache.put(k, v);
    }

    @Override
    public V evict(final K k) {
        final V value = this.get(k);
        cache.remove(k);
        return value;
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
        cache.clear();
    }
}
