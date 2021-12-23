package org.beyond.rbac.controller;

import org.beyond.rbac.annotation.AllowAnonymous;
import org.beyond.rbac.model.param.LoginParams;
import org.beyond.rbac.result.Result;
import org.beyond.rbac.service.AuthService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Beyond
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService authService) { this.authService = authService; }

    @AllowAnonymous
    @PostMapping
    public Result<String> login(@Validated @RequestBody LoginParams params) {
        String token = authService.login(params);
        return Result.data(token);
    }

}
