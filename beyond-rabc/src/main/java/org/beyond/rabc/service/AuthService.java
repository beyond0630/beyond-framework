package org.beyond.rabc.service;

import org.beyond.rabc.model.param.LoginParams;

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
