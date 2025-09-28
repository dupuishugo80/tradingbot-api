package com.tradingbot.tradingbot.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tradingbot.tradingbot.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecretValue;

    @Value("${jwt.expiration}")
    private int jwtExpirationMsValue;

    private static String jwtSecret;
    private static int jwtExpirationMs;
    private static SecretKey secretKey;

    @PostConstruct
    public void init() {
        jwtSecret = jwtSecretValue;
        jwtExpirationMs = jwtExpirationMsValue;
    }

    private static SecretKey getSecretKey() {
        if (secretKey == null) {
            secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        }
        return secretKey;
    }

    public String generateToken(User user) {
        return generateTokenWithExpiration(user, jwtExpirationMs);
    }

    public String generateRefreshToken(User user) {
        long refreshTokenExpirationMs = 7 * 24 * 60 * 60 * 1000;
        return generateTokenWithExpiration(user, refreshTokenExpirationMs);
    }

    public String generateTokenWithExpiration(User user, long expirationMs) {
        SecretKey secretKey = getSecretKey();
        return Jwts.builder()
            .setSubject(user.getUsername())
            .claim("userId", user.getId())
            .claim("roles", user.getRoles())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            SecretKey secretKey = getSecretKey();
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        List<?> roles = claims.get("roles", List.class);
        return roles.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList());
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    private Claims getClaimsFromToken(String token) {
        SecretKey secretKey = getSecretKey();
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
