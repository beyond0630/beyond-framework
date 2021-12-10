package org.beyond.rabc.biz;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

import org.beyond.rabc.constant.HttpAuthConstants;

import static org.springframework.web.servlet.HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE;

/**
 * @author Beyond
 */
public interface HttpAuthorizer {

    /**
     * 鉴权
     *
     * @param request request
     * @return boolean
     */
    boolean authorize(HttpServletRequest request);

    /**
     * 获取用户 Id
     *
     * @param request request
     * @return userId
     */
    default long getUserId(HttpServletRequest request) {
        Object attribute = request.getAttribute(HttpAuthConstants.HEADER_X_USER_ID);
        return (long) attribute;
    }

    /**
     * 获取请求方法
     *
     * @param request request
     * @return String
     */
    default String getMethod(HttpServletRequest request) {
        return request.getMethod().toUpperCase(Locale.ROOT);
    }

    /**
     * 获取 url
     *
     * @param request request
     * @return String
     */
    default String getUrl(HttpServletRequest request) {
        return request.getAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE).toString();
    }

}
