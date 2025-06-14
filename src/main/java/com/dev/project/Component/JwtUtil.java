package com.dev.project.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

	private final SecretKey key = Keys.hmacShaKeyFor("super_secret_key_123456789012345678901234".getBytes()); // At least 256 bits
	private final long EXPIRATION_MS = 24 * 60 * 60 * 1000; // 1 day

	// Generate token using name as subject
	public String generateToken(String name) {
		return Jwts.builder()
				.setSubject(name)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	// Extract name from token
	public String extractName(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// Validate token
	public boolean isTokenValid(String token, String username) {
		final String extractedName = extractName(token);
		return (extractedName.equals(username) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.setSigningKey(key)
				.parseClaimsJws(token)
				.getBody();
	}
}
