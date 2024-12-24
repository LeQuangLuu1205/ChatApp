package com.intern.ChatApp.utils;

import com.intern.ChatApp.enums.ErrorCode;
import com.intern.ChatApp.exception.AppException;
import com.intern.ChatApp.repository.InvalidatedTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtilsHelper {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long jwtExpirationInMs;
    @Autowired
    private InvalidatedTokenRepository invalidatedTokenRepository;
    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);

        return new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer("chat.com")
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        if (username == null || !username.equals(userDetails.getUsername()) || isTokenExpired(token)) {
            return false;
        }

        Claims claims = extractAllClaims(token);

        String tokenId = claims.getId();
        if (invalidatedTokenRepository.existsById(tokenId)) {
            return false;
        }

        return true;
    }

    public Boolean validateToken(String token) {
        final String username = extractUsername(token);

        Claims claims = extractAllClaims(token);

        String tokenId = claims.getId();
        if (invalidatedTokenRepository.existsById(tokenId)) {
            return false;
        }

        return true;
    }
}
