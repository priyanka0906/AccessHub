package com.priyanka.accesshub.controller;

import com.priyanka.accesshub.dto.request.PermissionIdDTO;
import com.priyanka.accesshub.dto.request.PermissionRequest;
import com.priyanka.accesshub.dto.response.PermissionResponse;
import com.priyanka.accesshub.dto.response.RoleResponse;
import com.priyanka.accesshub.service.PermissionService;
import com.priyanka.accesshub.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/permissions")
@Tag(name = "Permissions", description = "Endpoints for managing permissions and assigning them to roles")
public class PermissionController {

    private final PermissionService permissionService;
    private final RoleService roleService;

    public PermissionController(PermissionService permissionService, RoleService roleService){
        this.permissionService = permissionService;
        this.roleService = roleService;
    }

    @Operation(summary = "Create a new permission", description = "Adds a new permission to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permission created successfully"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping
    public Mono<ResponseEntity<PermissionResponse>> createPermission(@RequestBody PermissionRequest permissionRequest){
        return permissionService.createPermission(permissionRequest)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)));
    }

    @Operation(summary = "Assign permissions to a role", description = "Assigns one or more permissions to the specified role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permissions assigned successfully"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PreAuthorize("#clientId == authentication.principal.clientId")
    @PostMapping("/{clientId}/{roleId}")
    public Mono<ResponseEntity<RoleResponse>> assignPermissions(
            @PathVariable Long roleId,
            @PathVariable String clientId,
            @RequestBody PermissionIdDTO permissionIds) {
        return roleService.assignPermissionsToRole(roleId,permissionIds,clientId )
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)));
    }

    @Operation(summary = "Get permission by ID", description = "Retrieves details of a specific permission using its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permission retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Permission not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PreAuthorize("#clientId == authentication.principal.clientId")
    @GetMapping("/{clientId}/{permissionId}")
    public Mono<ResponseEntity<PermissionResponse>> getPermission(@PathVariable String clientId, @PathVariable Long permissionId){
        return permissionService.getPermission(permissionId,clientId)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)));
    }
}
