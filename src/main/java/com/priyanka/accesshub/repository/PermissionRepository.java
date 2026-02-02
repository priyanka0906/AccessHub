package com.priyanka.accesshub.repository;

import com.priyanka.accesshub.entity.Permission;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;


public interface PermissionRepository extends ReactiveCrudRepository<Permission,Long> {

    Flux<Permission> findAllByClientId(String clientId);
}
