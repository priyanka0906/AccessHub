package com.priyanka.accesshub.repository;

import com.priyanka.accesshub.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission,Long> {

    List<Permission>findAllByClientId(String clientId);
}
