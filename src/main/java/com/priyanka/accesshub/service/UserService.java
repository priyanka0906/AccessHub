package com.priyanka.accesshub.service;

import com.priyanka.accesshub.dto.response.UserResponse;
import com.priyanka.accesshub.entity.User;
import com.priyanka.accesshub.mapper.UserMapper;
import com.priyanka.accesshub.repository.UserRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserResponse> getAllUsers(String clientId) {
        List<User> users = userRepository.findAllByClientId(clientId);

        return userMapper.toUserResponse(users);
    }

    public UserResponse getUserByName(String clientId, String userName) {
        User user = userRepository.findByClientIdAndUserName(clientId,userName).orElseThrow();

        return userMapper.getUser(user);
    }

    public UserResponse getUserById(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();

        return userMapper.getUser(user);
    }
}
