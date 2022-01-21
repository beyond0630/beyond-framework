package org.beyond.rbac.controller;

import org.beyond.rbac.model.param.PermissionCode;
import org.beyond.rbac.result.Result;
import org.beyond.rbac.service.RolePermissionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Beyond
 */
@RestController
@RequestMapping("/api/roles/{roleCode}/permissions")
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    public RolePermissionController(final RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    @PostMapping
    public Result<?> grant(@PathVariable String roleCode,
                           @Validated @RequestBody PermissionCode params) {
        rolePermissionService.grant(roleCode, params.getCode());
        return Result.ok();
    }

    @DeleteMapping("/{rolePermissionId}")
    public Result<?> revoke(@PathVariable String roleCode,
                            @PathVariable int rolePermissionId) {
        rolePermissionService.revoke(rolePermissionId);
        return Result.ok();
    }

}
