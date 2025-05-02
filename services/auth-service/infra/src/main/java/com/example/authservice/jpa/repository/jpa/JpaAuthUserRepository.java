package com.example.authservice.jpa.repository.jpa;

import com.example.authservice.jpa.entity.AuthUserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JpaAuthUserRepository extends JpaRepository<AuthUserJpaEntity, UUID> {
    Optional<AuthUserJpaEntity> findByEmail(String email);
    @Query("SELECT u FROM AuthUserJpaEntity u JOIN u.refreshTokens t WHERE t.token = :token")
    Optional<AuthUserJpaEntity> findByRefreshToken(@Param("token") String token);

}
