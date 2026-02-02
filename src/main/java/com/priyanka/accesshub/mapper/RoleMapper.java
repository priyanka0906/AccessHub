package com.priyanka.accesshub.mapper;

import com.priyanka.accesshub.dto.response.RoleResponse;
import com.priyanka.accesshub.entity.Permission;
import com.priyanka.accesshub.entity.Role;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleMapper {
private final PermissionMapper permissionMapper;

    public RoleMapper(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    public RoleResponse toRoleResponse(Role role, Set<Permission>permissions){
        return RoleResponse.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .clientId(role.getClientId())
                .permissions(permissionMapper.getPermissions(permissions))
                .build();
    }
    public RoleResponse toRoleResponse(Role role){
        return RoleResponse.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .clientId(role.getClientId())
                .permissions(permissionMapper.getPermissions(Collections.emptySet()))
                .build();
    }


}
