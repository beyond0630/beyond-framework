package org.beyond.rbac.service;

import org.beyond.rbac.model.param.AddOrUpdateRole;

/**
 * @author Beyond
 */
public interface RoleService {

    /**
     * 保存角色
     *
     * @param params params
     */
    void saveRole(AddOrUpdateRole params);

    /**
     * 编辑角色
     *
     * @param id     角色 id
     * @param params 参数
     */
    void editRole(int id, AddOrUpdateRole params);

    /**
     * 删除角色
     *
     * @param id id
     */
    void deleteRole(int id);

}
