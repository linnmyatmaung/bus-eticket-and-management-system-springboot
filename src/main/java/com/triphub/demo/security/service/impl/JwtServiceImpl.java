package com.triphub.demo.security.service.impl;

import com.triphub.demo.api.Admin.model.Admin;
import com.triphub.demo.api.token.model.Token;
import com.triphub.demo.api.token.repository.TokenRepository;
import com.triphub.demo.config.utils.EntityUtil;
import com.triphub.demo.security.service.JwtService;
import com.triphub.demo.security.utils.ClaimProvider;
import com.triphub.demo.security.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.access-token-validity-ms}")
    private long accessTokenValidityMs;

    @Value("${jwt.refresh-token-validity-ms}")
    private long refreshTokenValidityMs;

    private final TokenRepository tokenRepository;



    private final Set<String> revokedTokens = ConcurrentHashMap.newKeySet();

    @Override
    public Claims validateToken(String token) {
        if (!JwtUtil.isTokenValid(token)) {
            throw new SecurityException("Invalid or expired token.");
        }

        if (isTokenRevoked(token)) {
            throw new SecurityException("Token has been revoked.");
        }

        return JwtUtil.decodeToken(token);
    }

    @Override
    public void revokeToken(String token) {
        revokedTokens.add(token);
    }

    @Override
    public boolean isTokenRevoked(String token) {
        return revokedTokens.contains(token);
    }


    @Override
    public Map<String, Object> generateTokens(Admin newAdmin) {

        String accessToken = generateAccessToken(newAdmin);
        String refreshToken;

        Optional<Token> existingToken = tokenRepository.findByAdmin(newAdmin);

        if (existingToken.isPresent()) {
            // ✅ Reuse existing refresh token
            refreshToken = existingToken.get().getRefreshToken();
        } else {
            // ✅ Create + save new refresh token
            refreshToken = JwtUtil.generateTokenForUser(newAdmin, newAdmin.getEmail(), refreshTokenValidityMs);

            Token token = Token.builder()
                    .admin(newAdmin)
                    .refreshToken(refreshToken)
                    .expiredAt(Instant.now().plus(7, ChronoUnit.DAYS))
                    .build();

            EntityUtil.saveEntityWithoutReturn(tokenRepository, token, "token");
        }

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }



    @Override
    public String generateAccessToken(Admin newAdmin){
        return  JwtUtil.generateTokenForUser(newAdmin,newAdmin.getEmail(),accessTokenValidityMs);
    }






    @Override
    public String extractUsername(String token) {
        Claims claims = JwtUtil.decodeToken(token);
        return claims.getSubject(); // subject = username in standard JWT
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        if (!JwtUtil.isTokenValid(token)) {
            return false;
        }

        if (isTokenRevoked(token)) {
            return false;
        }

        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername());
    }

}
