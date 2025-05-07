package com.example.practice.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.practice.entity.Users;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

//    public String generateToken(String username, Integer Id) {
    public String generateToken(Users users) {

//        long expirationTimeMs = 1000 * 60 * 60;
        long expirationTimeMs = 60000;

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTimeMs);
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("userid", users.getUserId());
        claims.put("email", users.getEmail());


        return Jwts.builder()
//                .claim("UserId", Id)
                .setClaims(claims)
                .setSubject(users.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

//    public Integer extractUserId(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(SECRET_KEY)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .get("userid", Integer.class);
//    }

    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }
    
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
