package org.beyond.rabc.repository;

import org.beyond.rabc.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Beyond
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {


    /**
     * 通过用户名查询是否存在
     *
     * @param username username;
     * @return boolean
     */
    boolean existsByUsername(String username);

    /**
     * 通过邮箱查询是否存在
     *
     * @param email email;
     * @return boolean
     */
    boolean existsByEmail(String email);

    /**
     * 通过用户名查找
     *
     * @param username username
     * @param deleted  deleted
     * @return user
     */
    User getByUsernameAndDeleted(String username, Boolean deleted);

}
