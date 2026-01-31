package com.priyanka.accesshub.service;

import com.priyanka.accesshub.dto.request.RoleRequest;
import com.priyanka.accesshub.dto.response.RoleResponse;
import com.priyanka.accesshub.dto.response.UserResponse;
import com.priyanka.accesshub.entity.Permission;
import com.priyanka.accesshub.entity.Role;
import com.priyanka.accesshub.entity.User;
import com.priyanka.accesshub.mapper.RoleMapper;
import com.priyanka.accesshub.mapper.UserMapper;
import com.priyanka.accesshub.repository.PermissionRepository;
import com.priyanka.accesshub.repository.RoleRepository;
import com.priyanka.accesshub.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class RoleService {

     private final RoleRepository roleRepository;
     private final UserRepository userRepository;
     private final PermissionRepository permissionRepository;
     private final RoleMapper roleMapper;
     private final UserMapper userMapper;

     public RoleService(RoleRepository roleRepository,
                        UserRepository userRepository,
                        PermissionRepository permissionRepository, RoleMapper roleMapper, UserMapper userMapper){
         this.roleRepository = roleRepository;
         this.userRepository = userRepository;
         this.permissionRepository = permissionRepository;
         this.roleMapper = roleMapper;
         this.userMapper = userMapper;
     }


    public UserResponse assignRolesToUser(UUID userId, List<Long> roleIds) {
        User user = userRepository.findById(userId).orElseThrow();
        List<Role> roles = roleRepository.findAllById(roleIds);
        user.getRoles().addAll(roles);
        User response =  userRepository.save(user);

        return userMapper.getUser(response);

    }

    public  Set<RoleResponse> getUserRoles(UUID userId) {
       User user = userRepository.findUserWithRolesAndPermissions(userId);
       Set<Role> roles = user.getRoles();
       return roleMapper.toRoles(roles);
    }

    public  RoleResponse createRole(RoleRequest request) {
        Role role = Role.builder()
                .roleName(request.getName())
                .clientId(request.getClientId())
                .build();

        Role response = roleRepository.save(role);
        return roleMapper.toRoleResponse(response);
     }

    public RoleResponse assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        List<Permission> permissions = permissionRepository.findAllById(permissionIds);

        if (permissions.size() != permissionIds.size()) {
            throw new RuntimeException("One or more permission IDs are invalid");
        }

        role.getPermissions().addAll(permissions);
        Role response =  roleRepository.save(role);
        return roleMapper.toRoleResponse(response);
    }


    public RoleResponse getRole(Long roleId) {

         Role role = roleRepository.findById(roleId).orElseThrow();
         return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAllRoles(String clientId) {

         List<Role> response = roleRepository.findAllByClientId(clientId);
         if(response==null) return Collections.emptyList();
         return response.stream().map(roleMapper::toRoleResponse
         ).toList();
    }
}
