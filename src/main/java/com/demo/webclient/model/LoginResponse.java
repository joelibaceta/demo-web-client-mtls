package com.demo.webclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
    @JsonProperty("access_token")
    private String accessToken;
    
    @JsonProperty("expires_in")
    private Integer expiresIn;
    
    private String scope;
    
    @JsonProperty("token_type")
    private String tokenType;

    public LoginResponse() {
    }

    public LoginResponse(String accessToken, Integer expiresIn, String scope, String tokenType) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public boolean isSuccess() {
        return accessToken != null && !accessToken.isEmpty();
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "accessToken='" + (accessToken != null ? accessToken.substring(0, Math.min(30, accessToken.length())) + "..." : "null") + '\'' +
                ", expiresIn=" + expiresIn +
                ", scope='" + scope + '\'' +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }
}
