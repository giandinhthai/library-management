package com.example.authservice.security.token;

import com.example.authservice.aggregate.RefreshToken;
import com.example.authservice.service.TokenGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class RefreshTokenGeneratorImpl implements TokenGenerator {

    @Value("${application.security.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;

    @Override
    public RefreshToken createRefreshToken(UUID userId) {
        Instant now = Instant.now();
        Instant expires = now.plusSeconds(refreshTokenValidityInSeconds);
        return new RefreshToken(now, expires);
    }
}