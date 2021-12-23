package org.beyond.rbac.service.impl;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.beyond.rbac.common.IdFactory;
import org.beyond.rbac.exception.ApiException;
import org.beyond.rbac.model.entity.User;
import org.beyond.rbac.model.param.SaveUserParams;
import org.beyond.rbac.repository.UserRepository;
import org.beyond.rbac.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Beyond
 */
@Service
public class UserServiceImpl implements UserService {

    private final IdFactory idFactory;
    private final UserRepository userRepository;

    public UserServiceImpl(final IdFactory idFactory, final UserRepository userRepository) {
        this.idFactory = idFactory;
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(final SaveUserParams params) {
        boolean existsUsername = userRepository.existsByUsername(params.getUsername());
        if (existsUsername) {
            throw new ApiException("用户名已存在");
        }
        boolean existsEmail = userRepository.existsByEmail(params.getEmail());
        if (existsEmail) {
            throw new ApiException("邮箱已被注册");
        }

        User user = new User();
        user.setId(idFactory.generate());
        user.setUsername(params.getUsername());
        user.setPassword(DigestUtils.sha1Hex(params.getPassword()));
        user.setEmail(params.getEmail());
        user.setRegisterTime(new Date());
        user.setCreatedBy(user.getId());
        user.setModifiedBy(user.getId());
        userRepository.save(user);
    }

}
