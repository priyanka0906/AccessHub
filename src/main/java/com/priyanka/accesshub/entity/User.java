package com.priyanka.accesshub.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @Column("id")
    private UUID id;

    @Column("client_id")
    private String clientId;

    @Column("user_name")
    private String userName;

    @Column("password")
    private String password;

    @Column("created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

}
