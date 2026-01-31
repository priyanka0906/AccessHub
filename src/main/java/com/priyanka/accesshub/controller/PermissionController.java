package com.priyanka.accesshub.controller;

import com.priyanka.accesshub.dto.request.PermissionRequest;
import com.priyanka.accesshub.dto.response.PermissionResponse;
import com.priyanka.accesshub.dto.response.RoleResponse;
import com.priyanka.accesshub.service.PermissionService;
import com.priyanka.accesshub.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionService permissionService;
    private final RoleService roleService;

    public PermissionController(PermissionService permissionService, RoleService roleService){
        this.permissionService = permissionService;
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<PermissionResponse> createPermission(@RequestBody PermissionRequest permissionRequest){
        return ResponseEntity.ok(permissionService.createPermission(permissionRequest));
    }

    // assign permission to a role
    @PostMapping("/{roleId}")
    public ResponseEntity<RoleResponse> assignPermissions(@PathVariable Long roleId, @RequestBody List<Long> permissionIds) {
        return ResponseEntity.ok(roleService.assignPermissionsToRole(roleId, permissionIds)); }


    @GetMapping("/{permissionId}")
    public ResponseEntity<PermissionResponse> getPermission(@PathVariable Long permissionId){
        return ResponseEntity.ok(permissionService.getPermission(permissionId));
    }


}
