package org.beyond.rbac.config;

import org.beyond.rbac.biz.HttpAuthenticator;
import org.beyond.rbac.biz.HttpAuthorizer;
import org.beyond.rbac.interceptor.AuthenticationInterceptor;
import org.beyond.rbac.interceptor.AuthorizationInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Beyond
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    public MvcConfig(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor(applicationContext.getBean(HttpAuthenticator.class)))
            .addPathPatterns("/api/**");
        registry.addInterceptor(new AuthorizationInterceptor(applicationContext.getBean(HttpAuthorizer.class)))
            .addPathPatterns("/api/**");
    }

}
