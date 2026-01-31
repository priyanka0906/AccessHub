package com.priyanka.accesshub.config;

import com.priyanka.accesshub.security.JwtAuthenticationEntryPoint;
import com.priyanka.accesshub.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.priyanka.accesshub.security.RestAccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint authEntryPoint;
    private final RestAccessDeniedHandler accessDeniedHandler;
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, JwtAuthenticationEntryPoint authEntryPoint, RestAccessDeniedHandler accessDeniedHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authEntryPoint = authEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    private static final String[] PUBLIC_ENDPOINTS = {
            "/client/**",
            "/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       http.csrf(AbstractHttpConfigurer::disable)
               .sessionManagement(sm-> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .authorizeHttpRequests(auth->auth.requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                       .anyRequest().authenticated())
               .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);

       return http.build();
    }

}
