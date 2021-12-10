package org.beyond.rabc.controller;

import org.beyond.rabc.annotation.AllowAnonymous;
import org.beyond.rabc.model.param.LoginParams;
import org.beyond.rabc.result.Result;
import org.beyond.rabc.service.AuthService;
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
