package com.tradingbot.tradingbot.model.dto.auth;

public class LogoutResponse {
    private String message;
        
    public LogoutResponse(String message) { this.message = message; }
    public String getMessage() { return message; }
}
