package com.priyanka.accesshub.service;

import com.priyanka.accesshub.dto.request.PermissionRequest;
import com.priyanka.accesshub.dto.response.PermissionResponse;
import com.priyanka.accesshub.entity.Permission;
import com.priyanka.accesshub.mapper.PermissionMapper;
import com.priyanka.accesshub.repository.PermissionRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionService(PermissionRepository permissionRepository,PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }


    public PermissionResponse createPermission(PermissionRequest request){
        Permission permissionEntity = permissionMapper.toPermissionEntity(request);
        Permission response =  permissionRepository.save(permissionEntity);
        return permissionMapper.toPermissionResponse(response);
    }

    public PermissionResponse getPermission(Long permissionId) {
        Permission permission =  permissionRepository.findById(permissionId).orElseThrow();

        return permissionMapper.toPermissionResponse(permission);
    }

    public @Nullable List<PermissionResponse> getAllPermissions(String clientId) {
        List<Permission> response = permissionRepository.findAllByClientId(clientId);
        return response.stream().map(permissionMapper::toPermissionResponse
        ).toList();
    }
}
