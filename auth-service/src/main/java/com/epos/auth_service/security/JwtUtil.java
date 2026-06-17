package com.epos.auth_service.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${epos.jwt.secret}")
    private String secret;

    @Value("${epos.jwt.expirationMinutes:360}")
    private long expirationMinutes;

    private Key key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email, String role, Long tenantId) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date exp = new Date(now + 1000L * 60 * expirationMinutes);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(issuedAt)
                .setExpiration(exp)
                .addClaims(Map.of(
                        "role", role,
                        "tenantId", tenantId == null ? 0 : tenantId
                ))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public String extractRole(String token) {
        Object role = parseClaims(token).get("role");
        return role == null ? null : role.toString();
    }

    public Long extractTenantId(String token) {
        Object tid = parseClaims(token).get("tenantId");
        if (tid == null) return 0L;
        if (tid instanceof Number n) return n.longValue();
        return Long.parseLong(tid.toString());
    }
}
