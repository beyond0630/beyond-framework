package org.beyond.rabc.service;

import org.beyond.rabc.model.param.AddOrUpdatePermission;

/**
 * @author Beyond
 */
public interface PermissionService {

    /**
     * 保存权限
     *
     * @param params params
     */
    void savePermission(AddOrUpdatePermission params);

    /**
     * 编辑权限
     *
     * @param id 权限 id
     * @param params params
     */
    void editPermission(int id, AddOrUpdatePermission params);

    /**
     * 删除权限
     *
     * @param id id
     */
    void deletePermission(int id);

}
