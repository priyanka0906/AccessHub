package com.priyanka.accesshub.repository;

import com.priyanka.accesshub.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    Optional<Client> findByClientId(String clientId);
}
