package com.priyanka.accesshub.service;

import com.priyanka.accesshub.dto.LoginResponse;
import com.priyanka.accesshub.dto.RefreshRequest;
import com.priyanka.accesshub.entity.User;
import com.priyanka.accesshub.repository.UserRepository;

import com.priyanka.accesshub.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.priyanka.accesshub.constant.UserConstants.*;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String register(String userName,String password){

        if(userRepository.findByUserName(userName).isPresent()){
            throw new IllegalArgumentException(ERROR_MSG);

        }

        User user = new User();
        user.setUserName(userName);
        user.setPassword(passwordEncoder.encode(password));
        persistenceToDatabase(user);
        return SUCCESS_MSG;

    }

    public LoginResponse login(String username, String password){

        User user = userRepository.findByUserName(username).orElseThrow(()->new IllegalArgumentException(INVALID_USERNAME));

        if(!passwordEncoder.matches(password,user.getPassword())){
            throw new IllegalArgumentException(INVALID_PASSWORD);
        }

     String accessToken = jwtUtil.generateToken(username);
     String refreshToken = jwtUtil.generateRefreshToken(username);

     return new LoginResponse(accessToken,refreshToken,BEARER,EXPIRES_IN);
    }

    private void persistenceToDatabase(User user){
        try {

            userRepository.save(user);
        }
        catch(Exception e){
            throw new RuntimeException("Error while saving user: "+ e.getMessage(),e);
        }
    }

    public LoginResponse refresh(String refreshToken) {
        String username = jwtUtil.extractUsername(refreshToken);

        if(jwtUtil.isTokenExpired(refreshToken)){
            throw new IllegalArgumentException(EXPIRED);
        }
        String newAccessToken = jwtUtil.generateToken(username);

        return new LoginResponse(newAccessToken,refreshToken,BEARER,EXPIRES_IN);
    }
}
