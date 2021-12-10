package org.beyond.rabc.interceptor;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.beyond.rabc.annotation.AllowAnonymous;
import org.beyond.rabc.biz.HttpAuthorizer;
import org.beyond.rabc.result.Result;
import org.beyond.rabc.utils.JsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author Beyond
 */
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    private final HttpAuthorizer httpAuthorizer;

    public AuthorizationInterceptor(final HttpAuthorizer httpAuthorizer) {
        this.httpAuthorizer = httpAuthorizer;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return super.preHandle(request, response, handler);
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        AllowAnonymous allowAnonymous = Optional.ofNullable(handlerMethod.getMethodAnnotation(AllowAnonymous.class))
            .orElseGet(() -> handlerMethod.getBeanType().getAnnotation(AllowAnonymous.class));
        if (allowAnonymous != null) {
            return super.preHandle(request, response, handler);
        }
        boolean ok = this.httpAuthorizer.authorize(request);
        if (!ok) {
            writeError(response, Result.make(HttpStatus.FORBIDDEN.getReasonPhrase(), "访问受限", null));
            return false;
        }
        return super.preHandle(request, response, handler);
    }

    private void writeError(final HttpServletResponse response, final Result<?> result) throws Exception {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(JsonUtils.serializeAsBytes(result));
    }

}
