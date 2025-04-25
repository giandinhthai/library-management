package com.example.authservice.aggregate;

import lombok.Getter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class RefreshToken {
    private final String token;
    private final Instant issuedAt;
    private final Instant expiresAt;
    private boolean revoked = false;
    public RefreshToken(String token, Instant issuedAt, Instant expiresAt, boolean revoked) {
        this.token = token;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.revoked = revoked;
    }
    public RefreshToken(Instant issuedAt, Instant expiresAt) {
        this.token = UUID.randomUUID().toString();
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public void revoke() {
        this.revoked = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefreshToken)) return false;
        RefreshToken that = (RefreshToken) o;
        return token.equals(that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

}