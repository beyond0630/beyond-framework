package org.beyond.rbac.service;

import org.beyond.rbac.model.param.SaveUserParams;

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
