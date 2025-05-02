package com.example.authservice.repositories;

import com.example.authservice.aggregate.AuthUser;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository {
    Optional<AuthUser> findByEmail(String email);
    Optional<AuthUser> findByUserId(UUID userId);
    void save(AuthUser authUser);
    Optional<AuthUser> findByRefreshToken(String refreshToken);
}