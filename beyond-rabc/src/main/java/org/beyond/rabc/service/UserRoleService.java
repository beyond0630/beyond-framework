package org.beyond.rabc.service;

import java.util.Set;

/**
 * @author Beyond
 */
public interface UserRoleService {

    /**
     * 分配角色
     *
     * @param userId   用户 Id
     * @param roleCode 角色 role
     */
    void assignRole(long userId, String roleCode);

    /**
     * 移除角色
     *
     * @param userRoleId 用户角色 id
     */
    void removeRole(int userRoleId);

    /**
     * 查询用户角色
     *
     * @param userId 用户 Id
     * @return 角色 set
     */
    Set<String> listRoles(long userId);

}
