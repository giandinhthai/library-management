package com.example.authservice.service;

import com.example.authservice.aggregate.RefreshToken;

import java.util.UUID;

public interface TokenGenerator {
    RefreshToken createRefreshToken(UUID userId);
}