package org.beyond.rabc.service;

import java.util.Set;

/**
 * @author Beyond
 */
public interface RolePermissionService {

    /**
     * 通过角色查询权限
     *
     * @param roleCode 角色编码
     * @return Set
     */
    Set<String> listPermissionsByRoleCode(String roleCode);

    /**
     * 角色授权
     *
     * @param roleCode       角色
     * @param permissionCode 权限
     */
    void grant(String roleCode, String permissionCode);

    /**
     * 撤消授权
     *
     * @param rolePermissionId 角色权限 Id
     */
    void revoke(int rolePermissionId);

}
