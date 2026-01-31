package com.priyanka.accesshub.controller;

import com.priyanka.accesshub.dto.response.RoleResponse;
import com.priyanka.accesshub.dto.response.UserResponse;
import com.priyanka.accesshub.service.RoleService;
import com.priyanka.accesshub.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
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

    @PostMapping("/{userId}")
    public ResponseEntity<UserResponse> assignRole(@PathVariable UUID userId, @RequestBody List<Long> roleIds){

        return ResponseEntity.ok(roleService.assignRolesToUser(userId,roleIds));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserRoles(@PathVariable UUID userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }

   @GetMapping("/{clientId}/{userName}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String clientId, @PathVariable String userName){
        return ResponseEntity.ok(userService.getUserByName(clientId,userName));
   }


}
