package com.triphub.demo.security.service;

import com.triphub.demo.api.Admin.model.Admin;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
    public String extractUsername(String token);

    public boolean isTokenValid(String token, UserDetails userDetails);

    public boolean isTokenRevoked(String token);

    public Map<String, Object> generateTokens(Admin admin);

    public void revokeToken(String token);

    public Claims validateToken(String token);

    public String generateAccessToken(Admin admin);


}
