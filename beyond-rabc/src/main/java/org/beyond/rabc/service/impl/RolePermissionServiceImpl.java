package org.beyond.rabc.service.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.beyond.rabc.config.CacheConfig;
import org.beyond.rabc.model.entity.RolePermission;
import org.beyond.rabc.repository.RolePermissionRepository;
import org.beyond.rabc.service.RolePermissionService;
import org.ehcache.Cache;
import org.springframework.stereotype.Service;

/**
 * @author Beyond
 */
@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    private final Cache<String, CacheConfig.RolePermissionWrapper> cache;
    private final RolePermissionRepository rolePermissionRepository;

    public RolePermissionServiceImpl(final Cache<String, CacheConfig.RolePermissionWrapper> cache,
                                     final RolePermissionRepository rolePermissionRepository) {
        this.cache = cache;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public Set<String> listPermissionsByRoleCode(final String roleCode) {
        CacheConfig.RolePermissionWrapper rolePermissionWrapper = cache.get(roleCode);
        if (rolePermissionWrapper != null) {
            return rolePermissionWrapper.getPermissions();
        }
        Set<String> permissions = rolePermissionRepository.getAllByRoleCode(roleCode)
            .stream()
            .filter(x -> !x.isDisabled())
            .map(RolePermission::getPermissionCode)
            .collect(Collectors.toSet());
        cache.put(roleCode, new CacheConfig.RolePermissionWrapper(permissions));
        return permissions;
    }

    @Override
    public void grant(final String roleCode, final String permissionCode) {
        RolePermission rolePermission = new RolePermission();
        rolePermission.setRoleCode(roleCode);
        rolePermission.setPermissionCode(permissionCode);
        rolePermissionRepository.save(rolePermission);
    }

    @Override
    public void revoke(final int rolePermissionId) {
        rolePermissionRepository.deleteById(rolePermissionId);
    }

}
