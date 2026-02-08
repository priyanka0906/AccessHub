package com.priyanka.accesshub.service;

import com.priyanka.accesshub.dto.request.PermissionRequest;
import com.priyanka.accesshub.dto.response.PermissionResponse;
import com.priyanka.accesshub.entity.Permission;
import com.priyanka.accesshub.mapper.PermissionMapper;
import com.priyanka.accesshub.repository.PermissionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionService(PermissionRepository permissionRepository,PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    @Transactional
    public Mono<PermissionResponse> createPermission(PermissionRequest request){
        Permission permissionEntity = permissionMapper.toPermissionEntity(request);
        return  permissionRepository.save(permissionEntity)
                .map(permissionMapper::toPermissionResponse)
               .onErrorMap(e -> new RuntimeException("Error while creating permission: " + e.getMessage(), e));

    }

    public Mono<PermissionResponse> getPermission(Long permissionId,String clientId) {
        return permissionRepository.findByIdAndClientId(permissionId, clientId)
                .switchIfEmpty(Mono.error(new RuntimeException("Permission not found")))
                .map(permissionMapper::toPermissionResponse);

    }

    public Flux<PermissionResponse> getAllPermissions(String clientId) {
        return permissionRepository.findAllByClientId(clientId)
                .map(permissionMapper::toPermissionResponse)
                .switchIfEmpty(Flux.empty());
    }
}
