package com.priyanka.accesshub.controller;

import com.priyanka.accesshub.dto.request.LoginDTO;
import com.priyanka.accesshub.dto.request.RefreshRequest;
import com.priyanka.accesshub.dto.request.RegisterDTO;
import com.priyanka.accesshub.dto.response.ErrorResponse;
import com.priyanka.accesshub.dto.response.RegisterResponse;
import com.priyanka.accesshub.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

   public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    Mono<ResponseEntity<RegisterResponse>> register(@RequestBody RegisterDTO request) {

    return authService.register(request)
             .map(message-> ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse(message)))
             .onErrorResume(IllegalArgumentException.class,e->Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegisterResponse(e.getMessage()))))
             .onErrorResume(RuntimeException.class, e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) .body(new RegisterResponse(e.getMessage()))));
    }
    @PostMapping("/login")
   public Mono<ResponseEntity<Object>> login(@RequestBody LoginDTO request){

     return authService.login(request)
             .map(loginResponse-> ResponseEntity.ok((Object)loginResponse))
             .onErrorResume(IllegalArgumentException.class,
                     e->Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body((Object) new ErrorResponse(e.getMessage()))));


    }
    @PostMapping("/refresh")
    public Mono<ResponseEntity<Object>> refresh(@RequestBody RefreshRequest request) {
      return authService.refresh(request)
              .map(refreshResponse->ResponseEntity.ok((Object)refreshResponse))
              .onErrorResume(IllegalArgumentException.class,
                      e->Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                              .body((Object) new ErrorResponse(e.getMessage()))));

    }
}
