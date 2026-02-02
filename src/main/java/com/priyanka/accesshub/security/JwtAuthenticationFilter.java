package com.priyanka.accesshub.security;

import com.priyanka.accesshub.dto.internal.UserPrincipal;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);
        try {
            if (!jwtUtil.isTokenExpired(token)) {
                String userName = jwtUtil.extractUsername(token);
                String clientId = jwtUtil.extractClientId(token);
                Set<String> roles = jwtUtil.extractRoles(token);
                Set<String> permissions = jwtUtil.extractPermissions(token);

                List<GrantedAuthority> authorities = new ArrayList<>();
                roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
                permissions.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission)));

                // Use custom principal instead of setDetails
                UserPrincipal principal = new UserPrincipal(userName, clientId);

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(principal, null, authorities);

                // Attach authentication to the reactive security context
                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
            }
        } catch (Exception ex) {
            // clear context by not attaching anything
        }

        return chain.filter(exchange);
    }
}

