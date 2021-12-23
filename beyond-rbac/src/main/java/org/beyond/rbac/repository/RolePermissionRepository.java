package org.beyond.rbac.repository;

import java.util.List;

import org.beyond.rbac.model.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Beyond
 */
public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer>, JpaSpecificationExecutor<RolePermission> {

    /**
     * 通过角色查询权限
     *
     * @param roleCode 角色编码
     * @return list
     */
    List<RolePermission> getAllByRoleCode(String roleCode);

    /**
     * 通过角色删除
     *
     * @param roleCode 角色 code
     * @return 删除行数
     */
    int deleteByRoleCode(String roleCode);

    /**
     * 通过角色删除
     *
     * @param permissionCode 权限 code
     * @return 删除行数
     */
    int deleteByPermissionCode(String permissionCode);

}
