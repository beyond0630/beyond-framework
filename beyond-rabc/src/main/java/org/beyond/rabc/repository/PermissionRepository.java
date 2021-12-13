package org.beyond.rabc.repository;

import org.beyond.rabc.model.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Beyond
 */
public interface PermissionRepository extends JpaRepository<Permission, Integer>, JpaSpecificationExecutor<Permission> {

    /**
     * 通过编码查询是否存在
     *
     * @param code code;
     * @return boolean
     */
    boolean existsByCode(String code);

    /**
     * 通过编码查询权限
     *
     * @param code code;
     * @return Permission
     */
    Permission getByCode(String code);

    /**
     * 通过 url 查询权限
     *
     * @param url url;
     * @return Permission
     */
    Permission getByUrl(String url);

}
