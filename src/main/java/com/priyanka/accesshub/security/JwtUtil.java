package com.priyanka.accesshub.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.expiration}")
    private long expirationMs;

    @Value("${jwt.refreshTokenExpiration}")
    private long refreshTokenExpirationMs;

      static final String TYPE ="type";
      static final String JWT = "JWT";

    private SecretKey getSigningKey()
    {
       return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String username,
                                String clientId,
                                Set<String>roles,
                                Set<String>permissions){
        Map<String,Object> claims = new HashMap<>();
        claims.put("clientId",clientId);
        claims.put("roles",roles);
        claims.put("permissions",permissions);

        return createToken(claims,username,expirationMs);
    }
    public String generateRefreshToken(String username,
                                       String clientId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("clientId", clientId);
        return createToken(claims, username, refreshTokenExpirationMs); }

    public String extractUsername(String token){
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
   }

   public String extractClientId(String token){
        return (String) extractAllClaims(token).get("clientId");
   }

   public Set<String> extractRoles(String token){
        return new HashSet<>((List<String>) extractAllClaims(token).get("roles"));
   }
    public Set<String> extractPermissions(String token){
        return new HashSet<>((List<String>) extractAllClaims(token).get("permissions"));
    }
   public boolean isTokenExpired(String token){
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
   }
   private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
   }
    private String createToken(Map<String,Object>claims,String username, long expiry){
        return Jwts.builder()
                .addClaims(claims)
                .setSubject(username)
                .setHeaderParam(TYPE,JWT)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(getSigningKey(),SignatureAlgorithm.HS256)
                .compact();
    }




}
