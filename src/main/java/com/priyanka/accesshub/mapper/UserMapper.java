package com.priyanka.accesshub.mapper;

import com.priyanka.accesshub.dto.request.RegisterDTO;
import com.priyanka.accesshub.dto.response.UserResponse;
import com.priyanka.accesshub.entity.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

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

    public List<UserResponse> toUserResponse(List<User>users){

        if(users==null||users.isEmpty())
            return Collections.emptyList();

        return users.stream().map(
                this::getUser
        ).toList();
    }
    public UserResponse getUser(User user){

         return UserResponse.builder()
                 .id(user.getId())
                 .userName(user.getUserName())
                 .createdAt(user.getCreatedAt())
                 .clientId(user.getClientId())
                 .roles(roleMapper.toRoles(user.getRoles()))
                 .build();
    }


}
