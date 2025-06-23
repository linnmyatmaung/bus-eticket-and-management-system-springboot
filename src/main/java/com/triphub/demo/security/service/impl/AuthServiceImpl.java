package com.triphub.demo.security.service.impl;

import com.triphub.demo.api.Admin.dto.AdminDto;
import com.triphub.demo.api.Admin.model.Admin;
import com.triphub.demo.api.Admin.repository.AdminRepository;
import com.triphub.demo.api.token.model.Token;
import com.triphub.demo.api.token.repository.TokenRepository;
import com.triphub.demo.config.response.dto.ApiResponse;
import com.triphub.demo.config.response.utils.ResponseUtil;
import com.triphub.demo.config.utils.EntityUtil;
import com.triphub.demo.config.utils.MapperUtil;
import com.triphub.demo.security.dto.LoginRequest;
import com.triphub.demo.security.dto.RegisterRequest;
import com.triphub.demo.security.service.AuthService;
import com.triphub.demo.security.service.JwtService;
import com.triphub.demo.security.utils.JwtUtil;
import com.triphub.demo.security.utils.PasswordValidatorUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final MapperUtil mapperUtil;

    @Override
    @Transactional
    public ApiResponse authenticateUser(LoginRequest loginRequest) {

        Admin admin = adminRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new SecurityException("Admin not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
            return ResponseUtil.error("Invalid email or password",HttpStatus.UNAUTHORIZED.value(), null);

        }

        Map<String, Object> tokens = jwtService.generateTokens(admin);

        AdminDto adminDto = mapperUtil.convertToDto(admin,AdminDto.class);

        log.info("Admin {} logged in successfully", admin.getEmail());
        return ApiResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .data(Map.of(
                        "user", adminDto,
                        "accessToken", tokens.get("accessToken"),
                        "refreshToken", tokens.get("refreshToken")
                ))
                .message("Logged in successfully")
                .build();
    }

    @Override
    @Transactional
    public ApiResponse registerUser(RegisterRequest registerRequest) {
        if (adminRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseUtil.error("Email already in use", HttpStatus.CONFLICT.value(), null);

        }

        if (!PasswordValidatorUtil.isValid(registerRequest.getPassword())) {
            return ResponseUtil.error("Password must include uppercase, lowercase, number, and special character", HttpStatus.BAD_REQUEST.value(), registerRequest.getPassword());
        }

        Admin newAdmin = Admin.builder()
                .adminName(registerRequest.getAdminName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        EntityUtil.saveEntityWithoutReturn(adminRepository, newAdmin, "newAdmin");

        Map<String, Object> tokens = jwtService.generateTokens(newAdmin);

        AdminDto adminDto = mapperUtil.convertToDto(newAdmin,AdminDto.class);

        log.info("User {} registered successfully", newAdmin.getEmail());
        return ApiResponse.builder()
                .success(true)
                .code(HttpStatus.CREATED.value())
                .data(Map.of(
                        "admin", adminDto,
                        "accessToken", tokens.get("accessToken"),
                        "refreshToken", tokens.get("refreshToken")
                ))
                .message("Registration successful")
                .build();
    }



    @Override
    @Transactional
    public ApiResponse logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseUtil.error("Invalid access token", HttpStatus.BAD_REQUEST.value(),null);
        }

        String accessToken = authHeader.substring(7);
        Claims claims = jwtService.validateToken(accessToken);
        String email = claims.getSubject();

        Admin admin = adminRepository.findByEmail(email).orElseThrow(() -> new SecurityException("Admin not found"));

        log.info("admin in logout {}",admin.getAdminName());

        // Delete refresh token (invalidate session)
        tokenRepository.deleteByAdminId(admin.getId());

        jwtService.revokeToken(accessToken);

        log.info("Admin {} logged out", email);
        return ApiResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .data(true)
                .message("Logout successful")
                .build();
    }

    @Override
    @Transactional
    public ApiResponse refreshToken(String refreshToken) {
        try {
            Claims claims = jwtService.validateToken(refreshToken);
            String email = claims.getSubject();
            log.info("email in refreshToken {}",email);

            Admin admin = adminRepository.findByEmail(email).orElseThrow(() -> new SecurityException("Admin not Found"));



            Token storedToken = tokenRepository.findByAdmin(admin)
                    .orElseThrow(() -> new SecurityException("Refresh token not found"));

            if (!storedToken.getRefreshToken().equals(refreshToken)) {
                return ResponseUtil.error("Invalid refresh token",HttpStatus.UNAUTHORIZED.value(), null);

            }

            String newAccessToken = jwtService.generateAccessToken(admin);


            log.info("Refreshed access token for {}", email);
            return ApiResponse.builder()
                    .success(true)
                    .code(HttpStatus.OK.value())
                    .data(Map.of("accessToken", newAccessToken))
                    .message("Access token refreshed successfully")
                    .build();

        } catch (SecurityException ex) {
            log.error("Refresh token validation failed: {}", ex.getMessage());
            return ResponseUtil.error("Invalid or expired refersh token",HttpStatus.UNAUTHORIZED.value(),null);

        }
    }

    @Override
    public ApiResponse getCurrentUser(String authHeader) {
        return null;
    }
}
