package com.tradingbot.tradingbot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tradingbot.tradingbot.model.dto.auth.AuthResponse;
import com.tradingbot.tradingbot.model.dto.auth.LoginRequest;
import com.tradingbot.tradingbot.model.dto.auth.LogoutResponse;
import com.tradingbot.tradingbot.model.dto.auth.RefreshTokenRequest;
import com.tradingbot.tradingbot.model.dto.auth.RegisterRequest;
import com.tradingbot.tradingbot.model.dto.auth.RegisterResponse;
import com.tradingbot.tradingbot.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        authService.register(
            request.getUsername(),
            request.getEmail(),
            request.getPassword()
        );
        return ResponseEntity.ok(new RegisterResponse("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(
            request.getUsername(),
            request.getPassword()
        );
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        AuthResponse authResponse = authService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@RequestBody RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(new LogoutResponse("Logged out successfully"));
    }
}
