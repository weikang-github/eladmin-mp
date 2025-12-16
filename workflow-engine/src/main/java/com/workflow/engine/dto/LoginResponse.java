package com.workflow.engine.dto;

/**
 * 登录响应DTO
 */
public class LoginResponse {
    
    private String token;
    private String username;
    private String roles;
    private String tokenType;
    private long expiresIn;
    
    public LoginResponse() {}
    
    public LoginResponse(String token, String username, String roles, String tokenType, long expiresIn) {
        this.token = token;
        this.username = username;
        this.roles = roles;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getRoles() {
        return roles;
    }
    
    public void setRoles(String roles) {
        this.roles = roles;
    }
    
    public String getTokenType() {
        return tokenType;
    }
    
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    
    public long getExpiresIn() {
        return expiresIn;
    }
    
    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
}