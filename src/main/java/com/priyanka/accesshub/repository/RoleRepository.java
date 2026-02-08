package com.priyanka.accesshub.repository;

import com.priyanka.accesshub.entity.Role;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface RoleRepository extends ReactiveCrudRepository<Role,Long> {

  Mono<Role> findByRoleNameAndClientId(String roleName, String clientId);
  Mono<Role> findByIdAndClientId(Long id, String clientId) ;
  Flux<Role>findAllByClientId(String clientId);


}
