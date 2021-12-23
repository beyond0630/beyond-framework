package org.beyond.rbac.config;

import org.beyond.rbac.common.IdFactory;
import org.beyond.rbac.common.impl.SnowflakeIdFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author Beyond
 */
@Configuration
public class BeanConfig {

    @Bean
    @Lazy
    public IdFactory idFactory() {
        return new SnowflakeIdFactory(0, 0);
    }

}
