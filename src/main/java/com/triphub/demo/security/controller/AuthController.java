package com.triphub.demo.security.controller;


import com.triphub.demo.config.response.dto.ApiResponse;
import com.triphub.demo.config.response.utils.ResponseUtil;
import com.triphub.demo.security.dto.LoginRequest;
import com.triphub.demo.security.dto.RefreshTokenData;
import com.triphub.demo.security.dto.RegisterRequest;
import com.triphub.demo.security.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${api.base.path}/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication related endpoints")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    @Operation(summary = "Login admin", description = "Authenticate admin with email and password")
    public ResponseEntity<ApiResponse> login(@Validated @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        log.info("Received login attempt for email: {}", loginRequest.getEmail());
        ApiResponse response = authService.authenticateUser(loginRequest);
        log.info(response.isSuccess() ? "Login successful for admin: {}" : "Login failed for admin: {}", loginRequest.getEmail());
        return ResponseUtil.buildResponse(request, response);
    }



    @PostMapping("/logout")
    @Operation(summary = "Logout admin", description = "Invalidate the current admin's session/token")
    public ResponseEntity<ApiResponse> logout(
            @RequestHeader(value = "Authorization", required = false) String accessToken,
            HttpServletRequest request) {
        log.info("Received logout request");
        ApiResponse response = authService.logout(accessToken);
        log.info(response.isSuccess() ? "Admin logout successfully" : "Logout failed for security reason");
        return ResponseUtil.buildResponse(request, response);
    }





    @PostMapping("/register")
    @Operation(summary = "Register new admin", description = "Create a new admin account")
    public ResponseEntity<ApiResponse> register(@Validated @RequestBody RegisterRequest registerRequest,
                                                HttpServletRequest request) {
        log.info("Received registration request for email: {}", registerRequest.getEmail());
        ApiResponse response = authService.registerUser(registerRequest);
        log.info(response.isSuccess() ? "User registered successfully: {}" : "Registration failed for email: {}", registerRequest.getEmail());
        return ResponseUtil.buildResponse(request, response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token", description = "Refresh JWT token using a valid refresh token")
    public ResponseEntity<ApiResponse> refresh(@Validated @RequestBody RefreshTokenData refreshTokenData,
                                               HttpServletRequest request) {
        log.info("Received token refresh request");
        ApiResponse response = authService.refreshToken(refreshTokenData.getRefreshToken());
        log.info(response.isSuccess()? "Token refreshed successfully" : "Token refresh failed");
        return ResponseUtil.buildResponse(request, response);
    }





}
