package org.beyond.rabc.service.impl;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.beyond.rabc.biz.TokenManager;
import org.beyond.rabc.model.entity.User;
import org.beyond.rabc.model.param.LoginParams;
import org.beyond.rabc.repository.UserRepository;
import org.beyond.rabc.service.AuthService;
import org.springframework.stereotype.Service;

/**
 * @author Beyond
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final TokenManager tokenManager;
    private final UserRepository userRepository;

    public AuthServiceImpl(final TokenManager tokenManager,
                           final UserRepository userRepository) {
        this.tokenManager = tokenManager;
        this.userRepository = userRepository;
    }

    @Override
    public String login(final LoginParams params) {
        User user = userRepository.getByUsernameAndDeleted(params.getUsername(), Boolean.FALSE);
        if (user == null || user.isDeleted() ||
            !StringUtils.equals(DigestUtils.sha1Hex(params.getPassword()), user.getPassword())) {
            throw new RuntimeException("账号或密码错误");
        }
        return tokenManager.createToken(user.getId());
    }

}
