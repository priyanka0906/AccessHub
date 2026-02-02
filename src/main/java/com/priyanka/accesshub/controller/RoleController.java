package com.priyanka.accesshub.controller;

import com.priyanka.accesshub.dto.request.RoleRequest;
import com.priyanka.accesshub.dto.response.RoleResponse;
import com.priyanka.accesshub.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;


    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @PostMapping
    public Mono<ResponseEntity<RoleResponse>> createRole( @RequestBody RoleRequest request) {
        return roleService.createRole(request)
                .map(roleResponse -> ResponseEntity.status(HttpStatus.CREATED).body(roleResponse))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)));
    }

    // Fetch role details with permissions
    @PreAuthorize("#clientId == authentication.principal.clientId")
    @GetMapping("/{clientId}/{roleId}")
    public Mono<ResponseEntity<RoleResponse>> getRole(@PathVariable String clientId,@PathVariable Long roleId) {
        return roleService.getRole(roleId)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)));
    }


}
