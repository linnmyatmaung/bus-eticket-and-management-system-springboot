package com.triphub.demo.security.utils;


import com.triphub.demo.api.Admin.model.Admin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final String SECRET = "bh5pYpZP4QuAlHFF4NljKIQD9QWU8HmX2wyKAiBaArk=";
    private static final Key SECRET_KEY;

    static {
        if (SECRET == null || SECRET.isEmpty()) {
            throw new IllegalStateException("JWT_SECRET_KEY environment variable is missing or empty!");
        }
        SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    private static final String ISSUER = "teamSmurfs-backend";

    public static String generateToken(Map<String, Object> claims, String subject, long expirationMillis) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(SECRET_KEY)
                .compact();
    }


    public static Claims decodeToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean isTokenValid(String token) {
        try {
            Claims claims = decodeToken(token);

            if (claims.getExpiration().before(new Date())) {
                return false;
            }

            if (!ISSUER.equals(claims.getIssuer())) {
                return false;
            }

            return claims.getSubject() != null && !claims.getSubject().isEmpty();
        } catch (ExpiredJwtException e) {
            return false; // Token expired
        } catch (JwtException e) {
            return false; // Invalid token (e.g., signature tampered)
        }
    }


    public static String generateTokenForUser(
            Admin admin,        // Admin/User etc
            String email,
            long validityMs
    ) {
        Map<String, Object> claims = ClaimProvider.generateClaims(admin);
        return generateToken(claims, email, validityMs);
    }


}