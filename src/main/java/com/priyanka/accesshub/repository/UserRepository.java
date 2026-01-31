package com.priyanka.accesshub.repository;

import com.priyanka.accesshub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUserNameAndClientId(String username,String clientId);

     @Query("SELECT DISTINCT u FROM User u " +
                "LEFT JOIN FETCH u.roles r " +
                "LEFT JOIN FETCH r.permissions " +
                "WHERE u.id = :id")
        User findUserWithRolesAndPermissions(@Param("id") UUID id);

     List<User> findAllByClientId(String clientId);

     Optional<User> findByClientIdAndUserName(String clientId,String userName);

}
