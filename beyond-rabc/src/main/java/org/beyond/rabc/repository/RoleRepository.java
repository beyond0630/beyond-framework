package org.beyond.rabc.repository;

import org.beyond.rabc.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Beyond
 */
public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

    /**
     * 查询角色
     *
     * @param code 角色编码
     * @return Role
     */
    Role getByCode(String code);

}
