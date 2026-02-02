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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/client")
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

    // Onboard a new Client
    @PostMapping
    public Mono<ResponseEntity<ClientResponse>> onboardClient(@RequestBody ClientDTO request){

        return clientService.onboardClient(request)
                .map(ResponseEntity::ok);
    }

    @PreAuthorize("#clientId == authentication.principal.clientId")
    @GetMapping("/{clientId}/users")
    public Flux<ResponseEntity<UserResponse>> getAllUsers(@PathVariable String clientId){
        return userService.getAllUsers(clientId)
                .map(ResponseEntity::ok)
                .onErrorResume(e ->
                        Flux.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)));
    }

    @PreAuthorize("#clientId == authentication.principal.clientId")
    @GetMapping("/{clientId}/roles")
    public Flux<ResponseEntity<RoleResponse>> getAllRole(@PathVariable String clientId) {
        return roleService.getAllRoles(clientId)
                .map(ResponseEntity::ok)
                .onErrorResume(e ->
                        Flux.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)));

    }

    @PreAuthorize("#clientId == authentication.principal.clientId")
    @GetMapping("/{clientId}/permissions")
    public Mono<ResponseEntity<List<PermissionResponse>>> getAllPermissions(@PathVariable String clientId){
        return permissionService.getAllPermissions(clientId)
                .collectList()
                .map(ResponseEntity::ok)
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)));

    }
}
