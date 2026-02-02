package com.priyanka.accesshub.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("users_roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {

     @Id
     private UUID id;

     @Column("user_id")
     private UUID userId;
     @Column("roles_id")
     private Long roleId;


}
