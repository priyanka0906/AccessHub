package com.priyanka.accesshub.repository;

import com.priyanka.accesshub.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

  Optional<Role> findByRoleNameAndClientId(String roleName, String clientId);

  @Query("SELECT r FROM Role r LEFT JOIN FETCH r.permissions WHERE r.id = :id")
  Role findRoleWithPermissions(@Param("id") Long id);

  List<Role>findAllByClientId(String clientId);

}
