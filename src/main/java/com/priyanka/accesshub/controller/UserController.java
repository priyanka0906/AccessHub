package com.priyanka.accesshub.controller;

import com.priyanka.accesshub.dto.response.UserResponse;
import com.priyanka.accesshub.service.RoleService;
import com.priyanka.accesshub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@Tag(name = "Users", description = "Endpoints for managing users and their roles")
public class UserController {

    private final RoleService roleService;
    private final UserService userService;

    public UserController(RoleService roleService, UserService userService){
        this.roleService = roleService;
        this.userService = userService;
    }

    @Operation(summary = "Assign roles to a user", description = "Assigns one or more roles to the specified user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles assigned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or user not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("#clientId == authentication.principal.clientId")
    @PostMapping("/{userId}")
    public Mono<ResponseEntity<UserResponse>> assignRole(
            @PathVariable UUID userId,
            @RequestBody List<Long> roleIds) {
        return roleService.assignRolesToUser(userId, roleIds)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)));
    }

    @Operation(summary = "Get user roles", description = "Fetches all roles assigned to the specified user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User roles retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or user not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("#clientId == authentication.principal.clientId")
    @GetMapping("/{userId}")
    public Mono<ResponseEntity<UserResponse>> getUserRoles(@PathVariable UUID userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)));
    }

    @Operation(summary = "Get user by name", description = "Fetches user details by clientId and userName")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or user not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("#clientId == authentication.principal.clientId")
    @GetMapping("/{clientId}/{userName}")
    public Mono<ResponseEntity<UserResponse>> getUser(
            @PathVariable String clientId,
            @PathVariable String userName) {
        return userService.getUserByName(clientId, userName)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)));
    }
}
