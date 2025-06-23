package com.triphub.demo.security.service;

import com.triphub.demo.config.response.dto.ApiResponse;
import com.triphub.demo.security.dto.LoginRequest;
import com.triphub.demo.security.dto.RegisterRequest;

public interface AuthService {

    ApiResponse authenticateUser(LoginRequest loginRequest);

    ApiResponse registerUser(RegisterRequest registerRequest);

    ApiResponse logout(String accessToken);

    ApiResponse refreshToken(String refreshToken);

    ApiResponse getCurrentUser(String authHeader);
}
