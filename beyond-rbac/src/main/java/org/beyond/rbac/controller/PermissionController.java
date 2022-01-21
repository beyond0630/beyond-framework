package org.beyond.rbac.controller;

import org.beyond.rbac.model.param.AddOrUpdatePermission;
import org.beyond.rbac.result.Result;
import org.beyond.rbac.service.PermissionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Beyond
 */
@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(final PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping
    public Result<?> addPermission(@Validated @RequestBody AddOrUpdatePermission params) {
        permissionService.savePermission(params);
        return Result.ok();
    }


    @PutMapping("/{permissionId}")
    public Result<?> editPermission(@PathVariable int permissionId,
                                    @Validated @RequestBody AddOrUpdatePermission params) {
        permissionService.editPermission(permissionId, params);
        return Result.ok();
    }

    @DeleteMapping("/{permissionId}")
    public Result<?> deletePermission(@PathVariable int permissionId) {
        permissionService.deletePermission(permissionId);
        return Result.ok();
    }

}
