package com.tradingbot.tradingbot.model.dto.auth;

public class ProfileResponse {
    private String username;
    private String email;
    private String roles;

    public ProfileResponse(String username, String email, String roles) {
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getRoles() {
        return roles;
    }

    public String getEmail() {
        return email;
    }
}
