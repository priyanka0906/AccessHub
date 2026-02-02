package com.priyanka.accesshub.mapper;

import com.priyanka.accesshub.dto.request.RegisterDTO;
import com.priyanka.accesshub.dto.response.RoleResponse;
import com.priyanka.accesshub.dto.response.UserResponse;
import com.priyanka.accesshub.entity.Role;
import com.priyanka.accesshub.entity.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

   private final RoleMapper roleMapper;

    public UserMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }


    public  User toUser(RegisterDTO request){

        return User.builder()
                .userName(request.getUserName())
                .password(request.getPassword())
                .clientId(request.getClientId())
                .build();
    }

    public UserResponse getUser(User user, Set<Role> roles){

         return UserResponse.builder()
                 .id(user.getId())
                 .userName(user.getUserName())
                 .createdAt(user.getCreatedAt())
                 .clientId(user.getClientId())
                 .roles(roles.stream().map(role->
                         roleMapper.toRoleResponse(role,Collections.emptySet())
                         ).collect(Collectors.toSet()))
                 .build();
    }

    public UserResponse toUserResponse(User user, Set<RoleResponse>roles){
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .clientId(user.getClientId())
                .createdAt(user.getCreatedAt())
                .roles(roles) .build();
    }


}
