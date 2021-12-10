package org.beyond.rabc.config;

import java.time.Duration;
import java.util.Set;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author Beyond
 */
@Configuration
public class CacheConfig {

    private static final int MAX_CACHE_ENTRIES = 1000000;
    private static final int EXPIRE_SECONDS = 60 * 60 * 24;
    private static final String USER_ROLE_CACHE_ALIAS = "user_role_cache";
    private static final String ROLE_PERMISSION_CACHE_ALIAS = "role_permission_cache";

    @Bean
    @Lazy
    public CacheManager cacheManager() {
        return CacheManagerBuilder.newCacheManagerBuilder()
            .withCache(USER_ROLE_CACHE_ALIAS,
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, UserRoleWrapper.class,
                        ResourcePoolsBuilder.heap(MAX_CACHE_ENTRIES))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(EXPIRE_SECONDS))).build()

            )
            .withCache(ROLE_PERMISSION_CACHE_ALIAS,
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, RolePermissionWrapper.class,
                        ResourcePoolsBuilder.heap(MAX_CACHE_ENTRIES))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(EXPIRE_SECONDS))).build()

            )
            .build(true);

    }

    @Bean
    @Lazy
    public Cache<Long, UserRoleWrapper> userRoleCache(CacheManager cacheManager) {
        return cacheManager.getCache(USER_ROLE_CACHE_ALIAS, Long.class, UserRoleWrapper.class);
    }

    @Bean
    @Lazy
    public Cache<String, RolePermissionWrapper> rolePermissionCache(CacheManager cacheManager) {
        return cacheManager.getCache(ROLE_PERMISSION_CACHE_ALIAS, String.class, RolePermissionWrapper.class);
    }

    public static final class UserRoleWrapper {

        private Set<String> roleCodes;

        public UserRoleWrapper() {
        }

        public UserRoleWrapper(final Set<String> roleCodes) {
            this.roleCodes = roleCodes;
        }

        public Set<String> getRoleCodes() {
            return roleCodes;
        }

        public void setRoleCodes(final Set<String> roleCodes) {
            this.roleCodes = roleCodes;
        }

    }

    public static final class RolePermissionWrapper {

        private Set<String> permissions;



        public RolePermissionWrapper(final Set<String> permissions) {
            this.permissions = permissions;
        }

        public Set<String> getPermissions() {
            return permissions;
        }

        public void setPermissions(final Set<String> permissions) {
            this.permissions = permissions;
        }

    }
}
