package com.priyanka.accesshub.controller;

import com.priyanka.accesshub.dto.request.LoginDTO;
import com.priyanka.accesshub.dto.request.RefreshRequest;
import com.priyanka.accesshub.dto.request.RegisterDTO;
import com.priyanka.accesshub.dto.response.ErrorResponse;
import com.priyanka.accesshub.dto.response.LoginResponse;
import com.priyanka.accesshub.dto.response.RegisterResponse;
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
    ResponseEntity<RegisterResponse> register(@RequestBody RegisterDTO request) {

      try {
           String message = authService.register(request);
          return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse(message));

      } catch(IllegalArgumentException e) {

          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegisterResponse(e.getMessage()));
      }catch (RuntimeException e) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RegisterResponse(e.getMessage())); }

    }
    @PostMapping("/login")
   public ResponseEntity<?> login(@RequestBody LoginDTO request){

       try {
           LoginResponse loginResponse =  authService.login(request);
           return ResponseEntity.ok(loginResponse);
       } catch(IllegalArgumentException e) {

           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage()));

        }


    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {

         try{
                LoginResponse loginResponse = authService.refresh(request);
                return ResponseEntity.ok(loginResponse);
         }catch(IllegalArgumentException e) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage()));

        }
    }


}
