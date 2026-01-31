package com.priyanka.accesshub.mapper;

import com.priyanka.accesshub.dto.response.RoleResponse;
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

    public RoleResponse toRoleResponse(Role role){
        return RoleResponse.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .clientId(role.getClientId())
                .permissions(permissionMapper.getPermissions(role.getPermissions()))
                .build();
    }

   public Set<RoleResponse> toRoles(Set<Role> roles) {
        if(roles==null||roles.isEmpty()) {
            return Collections.emptySet();
        }
        return roles.stream().map(this::toRoleResponse
        ).collect(Collectors.toSet());
    }

}
