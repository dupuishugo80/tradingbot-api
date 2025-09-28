package com.tradingbot.tradingbot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tradingbot.tradingbot.model.User;
import com.tradingbot.tradingbot.model.dto.auth.ProfileResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "*")
public class ProfileController {
    
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            ProfileResponse response = new ProfileResponse(
                user.getUsername(),
                user.getEmail(),
                String.join(",", user.getRoles())
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while fetching the profile");
        }
    }
    
}
