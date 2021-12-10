package org.beyond.rabc.service;

import org.beyond.rabc.model.param.SaveUserParams;

/**
 * @author Beyond
 */
public interface UserService {

    /**
     * 保存新用户
     *
     * @param params
     */
    void saveUser(SaveUserParams params);

}
