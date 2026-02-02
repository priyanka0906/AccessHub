package com.priyanka.accesshub.repository;

import com.priyanka.accesshub.entity.UserRole;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface UserRoleRepository extends ReactiveCrudRepository<UserRole,UUID> {

    @Query("SELECT * FROM users_roles where user_id = :userId")
    Flux<UserRole> findByUserId(UUID userId);
}
