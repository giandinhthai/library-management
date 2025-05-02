package com.example.authservice.service;

import com.example.authservice.aggregate.AuthUser;

public interface JwtAccessTokenGenerator {
    String generate(AuthUser user);
    boolean validateToken(String token);
}