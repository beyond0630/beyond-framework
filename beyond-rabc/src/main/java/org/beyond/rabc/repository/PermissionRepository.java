package org.beyond.rabc.repository;

import org.beyond.rabc.model.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Beyond
 */
public interface PermissionRepository extends JpaRepository<Permission, Integer>, JpaSpecificationExecutor<Permission> {

    /**
     * 通过 url 查询权限
     *
     * @param url url;
     * @return Permission
     */
    Permission getByUrl(String url);

}
