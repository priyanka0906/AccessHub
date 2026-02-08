package com.priyanka.accesshub.repository;

import com.priyanka.accesshub.entity.Permission;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


public interface PermissionRepository extends ReactiveCrudRepository<Permission,Long> {

    Flux<Permission> findAllByClientId(String clientId);
    Mono<Permission> findByIdAndClientId(Long permissionId, String clientId);
    Flux<Permission> findAllByIdInAndClientId(List<Long> permissionIds, String clientId);


}
