package com.tradingbot.tradingbot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tradingbot.tradingbot.model.dto.auth.AuthResponse;
import com.tradingbot.tradingbot.model.dto.auth.ErrorResponse;
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
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            authService.register(
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
            );
            return ResponseEntity.ok(new RegisterResponse("User registered successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse authResponse = authService.login(
                request.getUsername(),
                request.getPassword()
            );
            return ResponseEntity.ok(authResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            AuthResponse authResponse = authService.refreshAccessToken(request.getRefreshToken());
            return ResponseEntity.ok(authResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequest request) {
        try {
            authService.logout(request.getRefreshToken());
            return ResponseEntity.ok(new LogoutResponse("Logged out successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}
