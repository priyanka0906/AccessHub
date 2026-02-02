package com.priyanka.accesshub.controller;

import com.priyanka.accesshub.dto.request.PermissionRequest;
import com.priyanka.accesshub.dto.response.PermissionResponse;
import com.priyanka.accesshub.dto.response.RoleResponse;
import com.priyanka.accesshub.service.PermissionService;
import com.priyanka.accesshub.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
    public Mono<ResponseEntity<PermissionResponse>> createPermission(@RequestBody PermissionRequest permissionRequest){
        return permissionService.createPermission(permissionRequest)
                .map(ResponseEntity::ok)
                .onErrorResume(e->Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)));
    }

    // assign permission to a role
    @PostMapping("/{roleId}")
    public Mono<ResponseEntity<RoleResponse>> assignPermissions(@PathVariable Long roleId, @RequestBody List<Long> permissionIds) {
        return roleService.assignPermissionsToRole(roleId, permissionIds)
        .map(ResponseEntity::ok)
        .onErrorResume(e->Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)));

    }



    @GetMapping("/{permissionId}")
    public Mono<ResponseEntity<PermissionResponse>> getPermission(@PathVariable Long permissionId){
        return permissionService.getPermission(permissionId)
                .map(ResponseEntity::ok)
                .onErrorResume(e->Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)));

    }


}
