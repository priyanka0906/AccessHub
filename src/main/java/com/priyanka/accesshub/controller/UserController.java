package com.priyanka.accesshub.controller;

import com.priyanka.accesshub.dto.response.UserResponse;
import com.priyanka.accesshub.service.RoleService;
import com.priyanka.accesshub.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final RoleService roleService;
    private final UserService userService;


    public UserController(RoleService roleService, UserService userService){
        this.roleService = roleService;
        this.userService = userService;
    }

    @PreAuthorize("#clientId == authentication.principal.clientId")
    @PostMapping("/{userId}")
    public Mono<ResponseEntity<UserResponse>> assignRole(@PathVariable UUID userId, @RequestBody List<Long> roleIds){

        return roleService.assignRolesToUser(userId,roleIds)
                .map(ResponseEntity::ok)
                .onErrorResume(e-> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST) .body(null)));

    }

    @PreAuthorize("#clientId == authentication.principal.clientId")
    @GetMapping("/{userId}")
    public Mono<ResponseEntity<UserResponse>> getUserRoles(@PathVariable UUID userId){
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .onErrorResume(e-> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST) .body(null)));

    }

    @PreAuthorize("#clientId == authentication.principal.clientId")
   @GetMapping("/{clientId}/{userName}")
    public Mono<ResponseEntity<UserResponse>> getUser(@PathVariable String clientId, @PathVariable String userName){
        return userService.getUserByName(clientId,userName)
                .map(ResponseEntity::ok)
                .onErrorResume(e-> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST) .body(null)));

   }


}
