package org.beyond.rabc.controller;

import org.beyond.rabc.model.param.RoleCode;
import org.beyond.rabc.result.Result;
import org.beyond.rabc.service.UserRoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Beyond
 */
@RestController
@RequestMapping("/api/users/{userId}/roles")
public class UserRoleController {

    private final UserRoleService userRoleService;

    public UserRoleController(final UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @PostMapping
    public Result<?> assignRole(@PathVariable long userId,
                                @Validated @RequestBody RoleCode params) {
        userRoleService.assignRole(userId, params.getCode());
        return Result.ok();
    }

}
