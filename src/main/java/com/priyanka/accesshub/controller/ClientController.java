package com.priyanka.accesshub.controller;

import com.priyanka.accesshub.dto.request.ClientDTO;
import com.priyanka.accesshub.dto.response.ClientResponse;
import com.priyanka.accesshub.dto.response.PermissionResponse;
import com.priyanka.accesshub.dto.response.RoleResponse;
import com.priyanka.accesshub.dto.response.UserResponse;
import com.priyanka.accesshub.service.ClientService;
import com.priyanka.accesshub.service.PermissionService;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/client")
@Tag(name = "Client Management", description = "Endpoints for onboarding clients and managing users, roles, and permissions")
public class ClientController {

    private final ClientService clientService;
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final UserService userService;

    public ClientController(ClientService clientService, PermissionService permissionService, RoleService roleService, UserService userService){
        this.clientService = clientService;
        this.permissionService = permissionService;
        this.roleService = roleService;
        this.userService = userService;
    }

    @Operation(summary = "Onboard a new client", description = "Registers a new client in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client onboarded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid client data"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping
    public Mono<ResponseEntity<ClientResponse>> onboardClient(@RequestBody ClientDTO request) {
        return clientService.onboardClient(request)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Get all users for a client", description = "Retrieves all users associated with the given clientId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PreAuthorize("#clientId == authentication.principal.clientId")
    @GetMapping("/{clientId}/users")
    public Mono<ResponseEntity<List<UserResponse>>> getAllUsers(@PathVariable String clientId) {
        return userService.getAllUsers(clientId)
                .collectList()
                .map(ResponseEntity::ok)
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)));
    }

    @Operation(summary = "Get all roles for a client", description = "Retrieves all roles associated with the given clientId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PreAuthorize("#clientId == authentication.principal.clientId")
    @GetMapping("/{clientId}/roles")
    public Mono<ResponseEntity<List<RoleResponse>>> getAllRole(@PathVariable String clientId) {
        return roleService.getAllRoles(clientId)
                .collectList()
                .map(ResponseEntity::ok)
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)));
    }

    @Operation(summary = "Get all permissions for a client", description = "Retrieves all permissions associated with the given clientId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permissions retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PreAuthorize("#clientId == authentication.principal.clientId")
    @GetMapping("/{clientId}/permissions")
    public Mono<ResponseEntity<List<PermissionResponse>>> getAllPermissions(@PathVariable String clientId) {
        return permissionService.getAllPermissions(clientId)
                .collectList()
                .map(ResponseEntity::ok)
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)));
    }
}
