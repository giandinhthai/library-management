package com.example.authservice.jpa.entity;

import com.example.authservice.aggregate.RefreshToken;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
@Entity(name = "refresh_tokens")
public class RefreshTokenJpaEntity {
    @Id
    private String token;

    private Instant issuedAt;

    private Instant expiresAt;

    private boolean revoked;

    public static RefreshTokenJpaEntity fromDomain(RefreshToken token) {
        RefreshTokenJpaEntity entity = new RefreshTokenJpaEntity();
        entity.setToken(token.getToken());
        entity.setIssuedAt(token.getIssuedAt());
        entity.setExpiresAt(token.getExpiresAt());
        entity.setRevoked(token.isRevoked());
        return entity;
    }

    public RefreshToken toDomain() {
        return new RefreshToken(token, issuedAt, expiresAt, revoked);
    }
}
