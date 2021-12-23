package org.beyond.rbac.controller;

import org.beyond.rbac.model.param.RoleCode;
import org.beyond.rbac.result.Result;
import org.beyond.rbac.service.UserRoleService;
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
