package com.priyanka.accesshub.controller;

import com.priyanka.accesshub.dto.request.LoginDTO;
import com.priyanka.accesshub.dto.request.RefreshRequest;
import com.priyanka.accesshub.dto.request.RegisterDTO;
import com.priyanka.accesshub.dto.response.ErrorResponse;
import com.priyanka.accesshub.dto.response.RegisterResponse;
import com.priyanka.accesshub.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration, login, and token refresh")
public class AuthController {


    private final AuthService authService;

   public AuthController(AuthService authService){
        this.authService = authService;
    }

    @Operation(summary = "Register a new user", description = "Creates a new user account and returns a confirmation message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Server error") })
    @PostMapping("/register")
    Mono<ResponseEntity<RegisterResponse>> register(@RequestBody RegisterDTO request) {

    return authService.register(request)
             .map(message-> ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse(message)))
             .onErrorResume(IllegalArgumentException.class,e->Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegisterResponse(e.getMessage()))))
             .onErrorResume(RuntimeException.class, e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) .body(new RegisterResponse(e.getMessage()))));
    }

    @Operation(summary = "Login user", description = "Authenticates a user and returns a JWT access token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials") })
    @PostMapping("/login")
   public Mono<ResponseEntity<Object>> login(@RequestBody LoginDTO request){

     return authService.login(request)
             .map(loginResponse-> ResponseEntity.ok((Object)loginResponse))
             .onErrorResume(IllegalArgumentException.class,
                     e->Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body((Object) new ErrorResponse(e.getMessage()))));
    }

    @Operation(summary = "Refresh token", description = "Generates a new JWT access token using a valid refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid refresh token") })
    @PostMapping("/refresh")
    public Mono<ResponseEntity<Object>> refresh(@RequestBody RefreshRequest request) {
      return authService.refresh(request)
              .map(refreshResponse->ResponseEntity.ok((Object)refreshResponse))
              .onErrorResume(IllegalArgumentException.class,
                      e->Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                              .body((Object) new ErrorResponse(e.getMessage()))));

    }
}
