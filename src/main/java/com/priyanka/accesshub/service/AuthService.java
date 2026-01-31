package com.priyanka.accesshub.service;

import com.priyanka.accesshub.dto.response.LoginResponse;
import com.priyanka.accesshub.dto.request.LoginDTO;
import com.priyanka.accesshub.dto.request.RefreshRequest;
import com.priyanka.accesshub.dto.request.RegisterDTO;
import com.priyanka.accesshub.entity.Permission;
import com.priyanka.accesshub.entity.Role;
import com.priyanka.accesshub.entity.User;
import com.priyanka.accesshub.mapper.UserMapper;
import com.priyanka.accesshub.repository.RoleRepository;
import com.priyanka.accesshub.repository.UserRepository;

import com.priyanka.accesshub.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import static com.priyanka.accesshub.constant.UserConstants.*;

@Service
public class AuthService {

    @Value("${jwt.expiration}")
    Long expiresIn;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       UserMapper userMapper,
                       RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;

    }

    public String register(RegisterDTO request){

        if(userRepository.findByUserNameAndClientId(request.getUserName(),request.getClientId()).isPresent()){
            throw new IllegalArgumentException(ERROR_MSG);

        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = userMapper.toUser(request);

        // Assign default role USER for this client
         Role deafultRole = roleRepository.findByRoleNameAndClientId("USER",request.getClientId())
                         .orElseThrow(()-> new RuntimeException("Default Role not found"));

        user.getRoles().add(deafultRole);
        persistenceToDatabase(user);
        return SUCCESS_MSG;

    }

    public LoginResponse login(LoginDTO request){

        User user = userRepository.findByUserNameAndClientId(request.getUserName(),request.getClientId())
                .orElseThrow(()->new IllegalArgumentException(INVALID_USERNAME));

        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new IllegalArgumentException(INVALID_PASSWORD);
        }

        Set<String> roles = user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        Set<String> permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                        .map(Permission::getPermissionName)
                .collect(Collectors.toSet());

     String accessToken = jwtUtil.generateToken(request.getUserName(),request.getClientId(),roles,permissions);
     String refreshToken = jwtUtil.generateRefreshToken(request.getUserName(),request.getClientId());

     return new LoginResponse(accessToken,refreshToken,BEARER,expiresIn/1000);
    }

    private void persistenceToDatabase(User user){
        try {

            userRepository.save(user);
        }
        catch(Exception e){
            throw new RuntimeException("Error while saving user: "+ e.getMessage(),e);
        }
    }

    public LoginResponse refresh(RefreshRequest request) {
        if(jwtUtil.isTokenExpired(request.getRefreshToken())){
            throw new IllegalArgumentException(EXPIRED);
        }

        String username = jwtUtil.extractUsername(request.getRefreshToken());
        String clientId = jwtUtil.extractClientId(request.getRefreshToken());

        User user = userRepository.findByUserNameAndClientId(username,clientId).orElseThrow();

        Set<String> roles = user.getRoles()
                .stream().map(Role::getRoleName)
                .collect(Collectors.toSet());
        Set<String> permissions = user.getRoles().stream()
                .flatMap(r -> r.getPermissions().stream())
                .map(Permission::getPermissionName)
                .collect(Collectors.toSet());

        String newAccessToken = jwtUtil.generateToken(username,clientId,roles,permissions);

        return new LoginResponse(newAccessToken,request.getRefreshToken(),BEARER,expiresIn/1000);
    }
}
