package org.beyond.rbac.service;

import org.beyond.rbac.model.param.LoginParams;

/**
 * @author Beyond
 */
public interface AuthService {

    /**
     * 登录
     *
     * @param params 登录参数
     * @return token
     */
    String login(LoginParams params);

}
