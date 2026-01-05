package com.priyanka.accesshub.controller;

import com.priyanka.accesshub.dto.*;
import com.priyanka.accesshub.entity.User;
import com.priyanka.accesshub.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

   public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    ResponseEntity<RegisterResponse> register(@RequestBody UserDTO user) {

      try {
           String message = authService.register(user.getUserName(),user.getPassword());
          return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse(message));

      } catch(IllegalArgumentException e) {

          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegisterResponse(e.getMessage()));
      }catch (RuntimeException e) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RegisterResponse(e.getMessage())); }

    }
    @PostMapping("/login")
   public ResponseEntity<?> login(@RequestBody User user){

       try {
           LoginResponse loginResponse =  authService.login(user.getUserName(),user.getPassword());
           return ResponseEntity.ok(loginResponse);
       } catch(IllegalArgumentException e) {

           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage()));

        }


    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {

         try{
                LoginResponse loginResponse = authService.refresh(request.getRefreshToken());
                return ResponseEntity.ok(loginResponse);
         }catch(IllegalArgumentException e) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage()));

        }
    }


}
