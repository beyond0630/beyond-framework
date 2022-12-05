package com.beyond.cache.noop;

import com.beyond.cache.Cache;

import java.util.function.Supplier;

/**
 * @author beyond
 * @since 2022/12/2
 */
public class NoOpCache<K, V> implements Cache<K, V> {
    @Override
    public V get(final K k) {
        return null;
    }

    @Override
    public void put(final K k, final V v) {

    }

    @Override
    public V evict(final K k) {
        return null;
    }

    @Override
    public V getOrDefault(final K k, final V v) {
        return null;
    }

    @Override
    public V getIfAbsent(final K k, final Supplier<V> supplier) {
        return null;
    }

    @Override
    public void clear() {

    }
}
