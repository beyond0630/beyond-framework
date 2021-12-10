package org.beyond.rabc.repository;

import org.beyond.rabc.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Beyond
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User getByUsernameAndDeleted(String username, Boolean deleted);

}
