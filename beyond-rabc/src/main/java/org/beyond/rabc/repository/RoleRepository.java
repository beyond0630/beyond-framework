package org.beyond.rabc.repository;

import org.beyond.rabc.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Beyond
 */
public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

    /**
     * 通过编码查询是否存在
     *
     * @param code code;
     * @return boolean
     */
    boolean existsByCode(String code);

    /**
     * 查询角色
     *
     * @param code 角色编码
     * @return Role
     */
    Role getByCode(String code);

}
