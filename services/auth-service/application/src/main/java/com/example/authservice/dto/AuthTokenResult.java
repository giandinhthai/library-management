package com.example.authservice.dto;


public class AuthTokenResult {
    private final String accessToken;
    private final String refreshToken;

    public AuthTokenResult(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}