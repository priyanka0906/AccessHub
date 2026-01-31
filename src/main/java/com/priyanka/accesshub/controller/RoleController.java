package com.priyanka.accesshub.controller;

import com.priyanka.accesshub.dto.request.RoleRequest;
import com.priyanka.accesshub.dto.response.RoleResponse;
import com.priyanka.accesshub.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;


    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@RequestBody RoleRequest request) {
        return ResponseEntity.ok(roleService.createRole(request)); }

    // Fetch role details with permissions
    @GetMapping("/{roleId}")
    public ResponseEntity<RoleResponse> getRole(@PathVariable Long roleId) {
     return ResponseEntity.ok(roleService.getRole(roleId)); }


}
