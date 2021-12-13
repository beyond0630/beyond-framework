package org.beyond.rabc.service.impl;

import org.beyond.rabc.exception.ApiException;
import org.beyond.rabc.exception.DataNotFoundException;
import org.beyond.rabc.model.entity.Permission;
import org.beyond.rabc.model.param.AddOrUpdatePermission;
import org.beyond.rabc.repository.PermissionRepository;
import org.beyond.rabc.repository.RolePermissionRepository;
import org.beyond.rabc.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Beyond
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public PermissionServiceImpl(final PermissionRepository permissionRepository,
                                 final RolePermissionRepository rolePermissionRepository) {
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public void savePermission(final AddOrUpdatePermission params) {
        boolean exists = this.permissionRepository.existsByCode(params.getCode());
        if (exists) {
            throw new ApiException("已存在code = " + params.getCode() + "的数据");
        }
        Permission permission = new Permission();
        permission.setCode(params.getCode());
        permission.setName(params.getName());
        permission.setUrl(params.getUrl());
        permissionRepository.save(permission);
    }

    @Override
    public void editPermission(final int id, final AddOrUpdatePermission params) {
        Permission permission = permissionRepository.findById(id)
            .orElseThrow(DataNotFoundException::new);
        permission.setName(params.getName());
        permission.setUrl(params.getUrl());
        permissionRepository.save(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(final int id) {
        Permission permission = permissionRepository.findById(id)
            .orElseThrow(DataNotFoundException::new);
        rolePermissionRepository.deleteByPermissionCode(permission.getCode());
        permissionRepository.deleteById(id);
    }

}
