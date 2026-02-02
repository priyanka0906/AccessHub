package com.priyanka.accesshub.service;

import com.priyanka.accesshub.dto.request.RoleRequest;
import com.priyanka.accesshub.dto.response.RoleResponse;
import com.priyanka.accesshub.dto.response.UserResponse;
import com.priyanka.accesshub.entity.*;
import com.priyanka.accesshub.mapper.RoleMapper;
import com.priyanka.accesshub.mapper.UserMapper;
import com.priyanka.accesshub.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleService {

     private final RoleRepository roleRepository;
     private final UserRepository userRepository;
     private final PermissionRepository permissionRepository;
     private final RoleMapper roleMapper;
     private final UserMapper userMapper;
     private final UserRoleRepository userRoleRepository;
     private final RolePermissionRepository rolePermissionRepository;

     public RoleService(RoleRepository roleRepository,
                        UserRepository userRepository,
                        PermissionRepository permissionRepository, RoleMapper roleMapper,
                        UserMapper userMapper, UserRoleRepository userRoleRepository,
                        RolePermissionRepository rolePermissionRepository){
         this.roleRepository = roleRepository;
         this.userRepository = userRepository;
         this.permissionRepository = permissionRepository;
         this.roleMapper = roleMapper;
         this.userMapper = userMapper;
         this.userRoleRepository = userRoleRepository;
         this.rolePermissionRepository = rolePermissionRepository;
     }


    public Mono<UserResponse> assignRolesToUser(UUID userId, List<Long> roleIds) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .flatMap(user->
                    roleRepository.findAllById(roleIds).collect(Collectors.toSet())
                            .flatMap(roles-> {
                                if(roles.size()!=roleIds.size()){
                                    return Mono.error(new RuntimeException("One or more role IDs are invalid"));
                                }
                                return Flux.fromIterable(roles)
                                        .flatMap(role->userRoleRepository.save(
                                                UserRole.builder()
                                                        .userId(user.getId())
                                                        .roleId(role.getId())
                                                        .build()
                                        ))
                                        .then(Mono.just(userMapper.getUser(user,roles)));
                            })
                );
    }

    public Mono<Set<RoleResponse>> getUserRoles(UUID userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .flatMap(user ->
                        userRoleRepository.findByUserId(userId)
                                .flatMap(ur -> roleRepository.findById(ur.getRoleId()))
                                .collectList()
                                .flatMap(roles ->
                                        Flux.fromIterable(roles)
                                                .flatMap(role ->
                                                        rolePermissionRepository.findByRoleId(role.getId())
                                                                .flatMap(rp -> permissionRepository.findById(rp.getPermissionId()))
                                                                .collect(Collectors.toSet())
                                                                .map(permissions -> roleMapper.toRoleResponse(role, permissions))
                                                )
                                                .collect(Collectors.toSet()) // collect RoleResponses
                                )
                );
    }


    @Transactional
    public  Mono<RoleResponse> createRole(RoleRequest request) {
        Role role = Role.builder()
                .roleName(request.getName())
                .clientId(request.getClientId())
                .build();

        return roleRepository.save(role)
                .map(response->roleMapper.toRoleResponse(response,Collections.emptySet()));

     }

     @Transactional
    public Mono<RoleResponse> assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        return roleRepository.findById(roleId)
                .switchIfEmpty(Mono.error(new RuntimeException("Role not found")))
                .flatMap(role->
                        permissionRepository.findAllById(permissionIds).collect(Collectors.toSet())
                                .flatMap(permissions -> {
                                    if(permissions.size()!=permissionIds.size())
                                        return Mono.error(new RuntimeException("One or more permission IDs are invalid"));
                                   return Flux.fromIterable(permissions)
                                           .flatMap(permission -> rolePermissionRepository.save(
                                                   RolePermission.builder()
                                                           .roleId(role.getId())
                                                           .permissionId(permission.getId())
                                                           .build()
                                           ))
                                           .then(Mono.just(roleMapper.toRoleResponse(role,permissions)));
                                })
                );


    }


    public Mono<RoleResponse> getRole(Long roleId) {

        return roleRepository.findById(roleId)
                .switchIfEmpty(Mono.error(new RuntimeException("Role not found")))
                .flatMap(role->
                        rolePermissionRepository.findByRoleId(role.getId())
                .flatMap(rp->permissionRepository.findById(rp.getPermissionId()))
                                .collect(Collectors.toSet())
                                .map(permissions -> roleMapper.toRoleResponse(role,permissions))
                );

    }

    public Flux<RoleResponse> getAllRoles(String clientId) {
         return roleRepository.findAllByClientId(clientId)
                 .flatMap(role->
                         rolePermissionRepository.findByRoleId(role.getId())
                                 .flatMap(rp->permissionRepository.findById(rp.getPermissionId()))
                                 .collect(Collectors.toSet())
                                 .map(permissions -> roleMapper.toRoleResponse(role,permissions))
                         );

    }
}
