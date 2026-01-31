package com.priyanka.accesshub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {
    private Long id;

    private String clientId;

    private String roleName;

    private Set<PermissionResponse> permissions = new HashSet<>();
}
