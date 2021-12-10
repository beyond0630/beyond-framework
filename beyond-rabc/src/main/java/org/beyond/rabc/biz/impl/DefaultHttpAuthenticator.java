package org.beyond.rabc.biz.impl;

import javax.servlet.http.HttpServletRequest;

import org.beyond.rabc.biz.HttpAuthenticator;
import org.beyond.rabc.biz.TokenManager;
import org.beyond.rabc.constant.HttpAuthConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author Beyond
 */
@Component
public class DefaultHttpAuthenticator implements HttpAuthenticator {

    private final TokenManager tokenManager;

    public DefaultHttpAuthenticator(final TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public boolean authenticate(final HttpServletRequest request) {
        String token = this.getToken(request);
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        long userId = tokenManager.verifyToken(token);
        if (userId == -1) {
            return false;
        }
        request.setAttribute(HttpAuthConstants.HEADER_X_USER_ID, userId);
        return true;
    }

}
