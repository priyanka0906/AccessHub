package com.priyanka.accesshub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UUID id;

    private String clientId;

    private String userName;

    private OffsetDateTime createdAt;

    private Set<RoleResponse> roles = new HashSet<>();
}
