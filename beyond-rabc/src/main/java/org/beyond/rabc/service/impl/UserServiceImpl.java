package org.beyond.rabc.service.impl;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.beyond.rabc.common.IdFactory;
import org.beyond.rabc.model.entity.User;
import org.beyond.rabc.model.param.SaveUserParams;
import org.beyond.rabc.repository.UserRepository;
import org.beyond.rabc.service.UserService;
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
