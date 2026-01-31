package com.priyanka.accesshub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column
    private String clientId;

    @Column(unique = true, nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }
    public Set<Role> getRoles() {
        if (roles == null) {
            roles = new HashSet<>();
        }
        return roles;
    }
}
