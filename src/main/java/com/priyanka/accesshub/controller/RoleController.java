package com.priyanka.accesshub.controller;

import com.priyanka.accesshub.dto.request.RoleRequest;
import com.priyanka.accesshub.dto.response.RoleResponse;
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
@RequestMapping("/roles")
@Tag(name = "Roles", description = "Endpoints for creating and retrieving roles with permissions")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @Operation(summary = "Create a new role", description = "Creates a new role in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role created successfully"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping
    public Mono<ResponseEntity<RoleResponse>> createRole(@RequestBody RoleRequest request) {
        return roleService.createRole(request)
                .map(roleResponse -> ResponseEntity.status(HttpStatus.CREATED).body(roleResponse))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)));
    }

    @Operation(summary = "Get role details", description = "Fetches role details along with its permissions for a given clientId and roleId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @PreAuthorize("#clientId == authentication.principal.clientId")
    @GetMapping("/{clientId}/{roleId}")
    public Mono<ResponseEntity<RoleResponse>> getRole(@PathVariable String clientId, @PathVariable Long roleId) {
        return roleService.getRole(roleId)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)));
    }
}


