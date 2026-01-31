package com.priyanka.accesshub.mapper;

import com.priyanka.accesshub.dto.request.PermissionRequest;
import com.priyanka.accesshub.dto.response.PermissionResponse;
import com.priyanka.accesshub.entity.Permission;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PermissionMapper {

    public PermissionResponse toPermissionResponse(Permission permission){

        return PermissionResponse.builder()
                        .id(permission.getId())
                        .permissionName(permission.getPermissionName())
                        .clientId(permission.getClientId())
                        .build();
    }

    public Permission toPermissionEntity(PermissionRequest request) {

        Permission permission = new Permission();
        permission.setPermissionName(request.getName());
        permission.setClientId(request.getClientId());
        return permission;
    }

    public Set<PermissionResponse> getPermissions(Set<Permission> permissions) {
        if (permissions == null) {
            return Collections.emptySet(); }
        return permissions.stream().map(this::toPermissionResponse
        ).collect(Collectors.toSet());
    }
}
