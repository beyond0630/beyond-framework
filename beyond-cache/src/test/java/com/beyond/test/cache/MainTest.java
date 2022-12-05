package com.beyond.test.cache;

import com.beyond.cache.Cache;
import com.beyond.cache.caffeine.CaffeineCacheManger;
import com.beyond.cache.caffeine.CaffeineCacheOptions;

import java.time.Duration;

/**
 * @author beyond
 * @since 2022/12/2
 */
public class MainTest {
    public static void main(String[] args) {
        final CaffeineCacheManger cacheManger = new CaffeineCacheManger(new CaffeineCacheOptions() {
            @Override
            public int initialCapacity() {
                return 10;
            }

            @Override
            public Duration getRefreshAfterWrite() {
                return Duration.ofSeconds(2);
            }

            @Override
            public int getMaxCapacity() {
                return 200;
            }

            @Override
            public Duration getExpiration() {
                return Duration.ofSeconds(100);
            }
        });
        final Cache<String, String> cache = cacheManger.createCache(String.class, String.class);
        final Cache<String, String> cache2 = cacheManger.createCache(String.class, String.class);
        System.out.println(cache2 == cache);
    }


}
