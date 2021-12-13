package org.beyond.rabc.service.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.beyond.rabc.config.CacheConfig;
import org.beyond.rabc.exception.ApiException;
import org.beyond.rabc.exception.DataNotFoundException;
import org.beyond.rabc.model.entity.Role;
import org.beyond.rabc.model.entity.User;
import org.beyond.rabc.model.entity.UserRole;
import org.beyond.rabc.repository.RoleRepository;
import org.beyond.rabc.repository.UserRepository;
import org.beyond.rabc.repository.UserRoleRepository;
import org.beyond.rabc.service.UserRoleService;
import org.ehcache.Cache;
import org.springframework.stereotype.Service;

/**
 * @author Beyond
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final Cache<Long, CacheConfig.UserRoleWrapper> cache;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(final Cache<Long, CacheConfig.UserRoleWrapper> cache,
                               final UserRepository userRepository,
                               final RoleRepository roleRepository,
                               final UserRoleRepository userRoleRepository) {
        this.cache = cache;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void assignRole(final long userId, final String roleCode) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new DataNotFoundException("用户[id=" + userId + "]不存在"));

        Role role = roleRepository.getByCode(roleCode);
        if (role == null || role.isDisabled()) {
            throw new DataNotFoundException("角色[code=" + roleCode + "]不存在");
        }

        boolean exists = userRoleRepository.existsByUserIdAndRoleCode(userId, roleCode);
        if (exists) {
            throw new ApiException("用户[id=" + userId + "]已拥有该角色");
        }
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleCode(roleCode);
        userRoleRepository.save(userRole);
        cache.clear();
    }

    @Override
    public void removeRole(final int userRoleId) {
        userRoleRepository.deleteById(userRoleId);
        cache.clear();
    }

    @Override
    public Set<String> listRoles(final long userId) {
        CacheConfig.UserRoleWrapper userRoleWrapper = cache.get(userId);
        if (userRoleWrapper != null) {
            return userRoleWrapper.getRoleCodes();
        }
        Set<String> roleCodes = userRoleRepository.getAllByUserId(userId)
            .stream()
            .filter(x -> !x.isDisabled())
            .map(UserRole::getRoleCode)
            .collect(Collectors.toSet());
        cache.put(userId, new CacheConfig.UserRoleWrapper(roleCodes));
        return roleCodes;
    }

}
