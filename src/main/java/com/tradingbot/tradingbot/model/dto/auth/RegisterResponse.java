package com.tradingbot.tradingbot.model.dto.auth;

public class RegisterResponse {
    private String message;
    
    public RegisterResponse(String message) { this.message = message; }
    public String getMessage() { return message; }
}
