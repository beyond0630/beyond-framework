package org.beyond.rabc.repository;

import java.util.List;

import org.beyond.rabc.model.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Beyond
 */
public interface UserRoleRepository extends JpaRepository<UserRole, Integer>, JpaSpecificationExecutor<UserRole> {

    /**
     * 通过 userId 查询
     *
     * @param userId user Id
     * @return list
     */
    List<UserRole> getAllByUserId(long userId);

    /**
     * 通过角色删除
     *
     * @param roleCode 角色 code
     * @return 删除行数
     */
    int deleteByRoleCode(String roleCode);

}
