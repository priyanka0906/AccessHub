package com.priyanka.accesshub.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "role")
public class Role {

    @Id
    @Column("id")
    private Long id;

    @Column("role_name")
    private String roleName;

    @Column("client_id")
    private String clientId;


}
