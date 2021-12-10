package org.beyond.rabc.service.impl;

import org.beyond.rabc.exception.DataNotFoundException;
import org.beyond.rabc.model.entity.Role;
import org.beyond.rabc.model.param.AddOrUpdateRole;
import org.beyond.rabc.repository.RolePermissionRepository;
import org.beyond.rabc.repository.RoleRepository;
import org.beyond.rabc.repository.UserRoleRepository;
import org.beyond.rabc.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Beyond
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public RoleServiceImpl(final RoleRepository roleRepository,
                           final UserRoleRepository userRoleRepository,
                           final RolePermissionRepository rolePermissionRepository) {
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public void saveRole(final AddOrUpdateRole params) {
        Role role = new Role();
        role.setCode(params.getCode());
        role.setName(params.getName());
        roleRepository.save(role);
    }

    @Override
    public void editRole(final int id, final AddOrUpdateRole params) {
        Role role = roleRepository.findById(id)
            .orElseThrow(DataNotFoundException::new);
        role.setName(params.getName());
        roleRepository.save(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(final int id) {
        Role role = roleRepository.findById(id)
            .orElseThrow(DataNotFoundException::new);
        userRoleRepository.deleteByRoleCode(role.getCode());
        rolePermissionRepository.deleteByRoleCode(role.getCode());
        roleRepository.deleteById(id);
    }

}
