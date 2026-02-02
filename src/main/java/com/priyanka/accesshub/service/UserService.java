package com.priyanka.accesshub.service;

import com.priyanka.accesshub.dto.response.UserResponse;
import com.priyanka.accesshub.mapper.RoleMapper;
import com.priyanka.accesshub.mapper.UserMapper;

import com.priyanka.accesshub.repository.PermissionRepository;
import com.priyanka.accesshub.repository.RolePermissionRepository;
import com.priyanka.accesshub.repository.RoleRepository;
import com.priyanka.accesshub.repository.UserRepository;
import com.priyanka.accesshub.repository.UserRoleRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;

    public UserService(UserRepository userRepository, UserMapper userMapper, RoleMapper roleMapper, UserRoleRepository userRoleRepository, RoleRepository roleRepository, RolePermissionRepository rolePermissionRepository, PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.permissionRepository = permissionRepository;
    }

    public Flux<UserResponse> getAllUsers(String clientId) {
        return userRepository.findAllByClientId(clientId)
                .flatMap(user ->
                        userRoleRepository.findByUserId(user.getId())
                                .flatMap(ur -> roleRepository.findById(ur.getRoleId()))
                                .collectList()
                                .flatMap(roles ->
                                        Flux.fromIterable(roles)
                                                .flatMap(role ->
                                                        rolePermissionRepository.findByRoleId(role.getId())
                                                                .flatMap(rp -> permissionRepository.findById(rp.getPermissionId()))
                                                                .collect(Collectors.toSet())
                                                                .map(perms -> roleMapper.toRoleResponse(role, perms))
                                                )
                                                .collect(Collectors.toSet()) // collect all RoleResponses
                                                .map(roleResponses -> userMapper.toUserResponse(user, roleResponses))
                                )
                );
    }


    public Mono<UserResponse> getUserByName(String clientId, String userName) {
        return userRepository.findByClientIdAndUserName(clientId, userName)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .flatMap(user ->
                        userRoleRepository.findByUserId(user.getId())
                                .flatMap(ur -> roleRepository.findById(ur.getRoleId()))
                                .collectList()
                                .flatMap(roles ->
                                        Flux.fromIterable(roles)
                                                .flatMap(role ->
                                                        rolePermissionRepository.findByRoleId(role.getId())
                                                                .flatMap(rp -> permissionRepository.findById(rp.getPermissionId()))
                                                                .collect(Collectors.toSet())
                                                                .map(perms -> roleMapper.toRoleResponse(role, perms))
                                                )
                                                .collect(Collectors.toSet())
                                                .map(roleResponses -> userMapper.toUserResponse(user, roleResponses))
                                )
                );
    }

    public Mono<UserResponse> getUserById(UUID userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .flatMap(user ->
                        userRoleRepository.findByUserId(user.getId())
                                .flatMap(ur -> roleRepository.findById(ur.getRoleId()))
                                .collectList()
                                .flatMap(roles ->
                                        Flux.fromIterable(roles)
                                                .flatMap(role ->
                                                        rolePermissionRepository.findByRoleId(role.getId())
                                                                .flatMap(rp -> permissionRepository.findById(rp.getPermissionId()))
                                                                .collect(Collectors.toSet())
                                                                .map(perms -> roleMapper.toRoleResponse(role, perms))
                                                )
                                                .collect(Collectors.toSet())
                                                .map(roleResponses -> userMapper.toUserResponse(user, roleResponses))
                                )
                );
    }

}
