package com.priyanka.accesshub.repository;

import com.priyanka.accesshub.entity.RolePermission;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface RolePermissionRepository extends ReactiveCrudRepository<RolePermission, UUID> {

    @Query("SELECT * FROM role_permissions WHERE role_id = :roleId")
    Flux<RolePermission> findByRoleId(Long roleId);
}
