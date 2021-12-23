package org.beyond.rbac.biz.impl;

import javax.servlet.http.HttpServletRequest;

import org.beyond.rbac.biz.AccessManager;
import org.beyond.rbac.biz.HttpAuthorizer;
import org.springframework.stereotype.Service;

/**
 * @author Beyond
 */
@Service
public class HttpAuthorizerImpl implements HttpAuthorizer {

    private final AccessManager accessManager;

    public HttpAuthorizerImpl(final AccessManager accessManager) {
        this.accessManager = accessManager;
    }

    @Override
    public boolean authorize(final HttpServletRequest request) {
        String method = this.getMethod(request);
        String url = this.getUrl(request);
        long userId = this.getUserId(request);
        return accessManager.userHasPermission(userId,
            String.format("%s-%s", method, url));
    }

}
