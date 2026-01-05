package com.priyanka.accesshub.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    public String generateToken(String username){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,username);
    }
   public String extractUsername(String token){
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
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
    private String createToken(Map<String,Object>claims,String username){
        return Jwts.builder()
                .addClaims(claims)
                .setSubject(username)
                .setHeaderParam(TYPE,JWT)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(),SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setHeaderParam(TYPE,JWT)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationMs))
                .signWith(getSigningKey(),SignatureAlgorithm.HS256)
                .compact();
    }


}
