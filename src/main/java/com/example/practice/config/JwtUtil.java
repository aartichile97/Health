package com.example.practice.config;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.example.practice.entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

//	private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	private static final String SECRET = "Xh92Kf7zLmW3pA8qRt5YvBnCd6EzGjU1Xh92Kf7zLmW3pA8qRt5YvBnCd6EzGjU1";

	
//	@Value("${jwt.secret}")
//	private String secret;

	private Key secretKey;
	long expirationTimeMs = 1000 * 60 * 60 * 24;

	@PostConstruct
	public void init() {
		this.secretKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(String username, Users users) {

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expirationTimeMs);

		Map<String, Object> claims = new HashMap<>();
		claims.put("userId", users.getUserId());
		claims.put("email", users.getEmail());
		claims.put("role", users.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }
    
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
	}

	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}

	public Integer extractUserId(String token) {
		return extractAllClaims(token).get("userId", Integer.class);
	}

	public String extractEmail(String token) {
		return extractAllClaims(token).get("email", String.class);
	}

	public String extractRole(String token) {
		return extractAllClaims(token).get("role", String.class);
	}

	public boolean validateToken(String token, String expectedUsername, Integer expectedUserId, String expectedEmail,
			String expectedRole) {
		String username = extractUsername(token);
		Integer userId = extractUserId(token);
		String email = extractEmail(token);
		String role = extractRole(token);

		return expectedUsername.equals(username) && expectedUserId.equals(userId) && expectedEmail.equals(email)
				&& expectedRole.equals(role) && !isTokenExpired(token);

	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Date extractExpiration(String token) {
		return extractAllClaims(token).getExpiration();
	}
}
