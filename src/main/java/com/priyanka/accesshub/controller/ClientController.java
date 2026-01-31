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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ClientResponse> onboardClient(@RequestBody ClientDTO request){

        return ResponseEntity.ok(clientService.onboardClient(request));
    }

    @GetMapping("/{clientId}/users")
    public ResponseEntity<List<UserResponse>> getAllUsers(@PathVariable String clientId){
        return ResponseEntity.ok(userService.getAllUsers(clientId));
    }

    @GetMapping("/{clientId}/roles")
    public ResponseEntity<List<RoleResponse>> getAllRole(@PathVariable String clientId) {
        return ResponseEntity.ok(roleService.getAllRoles(clientId)); }

    @GetMapping("/{clientId}/permissions")
    public ResponseEntity<List<PermissionResponse>> getAllPermissions(@PathVariable String clientId){
        return ResponseEntity.ok(permissionService.getAllPermissions(clientId));
    }
}
