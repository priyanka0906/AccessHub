package com.priyanka.accesshub.repository;

import com.priyanka.accesshub.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, UUID> {

     Mono<User> findByUserNameAndClientId(String username, String clientId);

     Flux<User> findAllByClientId(String clientId);

     Mono<User> findByClientIdAndUserName(String clientId,String userName);

}
