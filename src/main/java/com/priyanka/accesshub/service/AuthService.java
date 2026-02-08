package com.priyanka.accesshub.service;

import com.priyanka.accesshub.dto.response.LoginResponse;
import com.priyanka.accesshub.dto.request.LoginDTO;
import com.priyanka.accesshub.dto.request.RefreshRequest;
import com.priyanka.accesshub.dto.request.RegisterDTO;
import com.priyanka.accesshub.entity.*;
import com.priyanka.accesshub.mapper.UserMapper;
import com.priyanka.accesshub.repository.*;

import com.priyanka.accesshub.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    private final PermissionRepository permissionRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       UserMapper userMapper,
                       RoleRepository roleRepository, PermissionRepository permissionRepository, UserRoleRepository userRoleRepository, RolePermissionRepository rolePermissionRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRoleRepository = userRoleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Transactional
    public Mono<String> register(RegisterDTO request){
       return userRepository.findByUserNameAndClientId(request.getUserName(), request.getClientId())
               .flatMap(existingUser-> Mono.<String>error(new IllegalArgumentException(ERROR_MSG)))
               .switchIfEmpty(
                       Mono.defer(()-> {
                           request.setPassword(passwordEncoder.encode(request.getPassword()));
                           User user = userMapper.toUser(request);

                           return roleRepository.findByRoleNameAndClientId(USER, request.getClientId())
                                   .switchIfEmpty(Mono.error(new IllegalArgumentException(ERROR_MSG)))
                                   .flatMap(defaultRole->
                                      persistenceToDatabase(user)
                                              .flatMap(savedUser->
                                                      userRoleRepository.save(UserRole.builder()
                                                              .userId(savedUser.getId())
                                                              .roleId(defaultRole.getId())
                                                      .build()
                                              )
                                          )
                                              .thenReturn(SUCCESS_MSG)
                                   );


                                   })
               );



    }

    @Transactional
    public Mono<LoginResponse> login(LoginDTO request){
        return userRepository.findByUserNameAndClientId(request.getUserName(),request.getClientId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException(INVALID_USERNAME)))
                .flatMap(user->{
                    if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
                        return Mono.error(new IllegalArgumentException(INVALID_PASSWORD));
                    }

                    return userRoleRepository.findByUserId(user.getId())
                            .flatMap(userRole-> roleRepository.findById(userRole.getRoleId()))
                            .collectList()
                            .flatMap(roles->{
                                Set<String> roleNames = roles.stream().map(Role::getRoleName)
                                        .collect(Collectors.toSet());

                               return Flux.fromIterable(roles)
                                       .flatMap(role->rolePermissionRepository.findByRoleId(role.getId()))
                                       .flatMap(rp->permissionRepository.findById(rp.getPermissionId()))
                                       .map(Permission::getPermissionName)
                                       .collect(Collectors.toSet())
                                       .map(permissions->{
                                           String accessToken = jwtUtil.generateToken(request.getUserName(),request.getClientId(),roleNames,permissions);
                                           String refreshToken = jwtUtil.generateRefreshToken(request.getUserName(),request.getClientId());
                                            return new LoginResponse(accessToken,refreshToken,BEARER, expiresIn/1000);
                                       });
                            });
                      });
       }

    private Mono<User> persistenceToDatabase(User user){
        return userRepository.save(user)
                .onErrorMap(e-> new RuntimeException("Error while saving user: " + e.getMessage(), e));
    }
    public Mono<LoginResponse> refresh(RefreshRequest request) {
        if (jwtUtil.isTokenExpired(request.getRefreshToken())) {
            throw new IllegalArgumentException(EXPIRED);
        }

        String username = jwtUtil.extractUsername(request.getRefreshToken());
        String clientId = jwtUtil.extractClientId(request.getRefreshToken());

        return userRepository.findByUserNameAndClientId(username, clientId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(INVALID_USERNAME)))
                .flatMap(user ->
                        userRoleRepository.findByUserId(user.getId())
                                .flatMap(userRole -> roleRepository.findById(userRole.getRoleId()))
                                .collectList()
                                .flatMap(roles -> {
                                    Set<String> roleNames = roles.stream()
                                            .map(Role::getRoleName)
                                            .collect(Collectors.toSet());

                                    return Flux.fromIterable(roles)
                                            .flatMap(role -> rolePermissionRepository.findByRoleId(role.getId()))
                                            .flatMap(rp -> permissionRepository.findById(rp.getPermissionId()))
                                            .map(Permission::getPermissionName)
                                            .collect(Collectors.toSet())
                                            .map(permissions -> {
                                                String newAccessToken = jwtUtil.generateToken(
                                                        username,
                                                        clientId,
                                                        roleNames,
                                                        permissions
                                                );
                                                return new LoginResponse(
                                                        newAccessToken,
                                                        request.getRefreshToken(),
                                                        BEARER,
                                                        expiresIn / 1000
                                                );
                                            });
                                })
                );
    }


}
