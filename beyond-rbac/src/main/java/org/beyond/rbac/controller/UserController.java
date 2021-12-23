package org.beyond.rbac.controller;

import org.beyond.rbac.annotation.AllowAnonymous;
import org.beyond.rbac.model.param.SaveUserParams;
import org.beyond.rbac.result.Result;
import org.beyond.rbac.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Beyond
 */
@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;

    public UserController(final UserService userService) { this.userService = userService; }

    @AllowAnonymous
    @PostMapping
    public Result<?> saveUser(@Validated @RequestBody SaveUserParams params) {
        userService.saveUser(params);
        return Result.ok();
    }

}
