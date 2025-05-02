package com.example.authservice.jpa.mapper;

import com.example.authservice.aggregate.AuthUser;
import com.example.authservice.jpa.entity.AuthUserJpaEntity;
import com.example.authservice.jpa.entity.RefreshTokenJpaEntity;
//TODO: lib for handle entity => DTO
import java.lang.reflect.Constructor;
import java.util.stream.Collectors;
public class AuthUserMapper {
    public static AuthUser toDomain(AuthUserJpaEntity entity) {
        // Use the reconstitute method to create the domain object from persistence
        AuthUser user = AuthUser.reconstitute(entity.getUserId(), entity.getEmail(), entity.getHashedPassword());

        // Add roles and tokens
        entity.getRoles().forEach(user::assignRole);
        entity.getRefreshTokens().forEach(rt -> user.addRefreshToken(rt.toDomain()));

        // Set active status
        if (!entity.isActive()) user.deactivate();

        return user;
    }

    public static AuthUserJpaEntity toJpa(AuthUser user) {
        AuthUserJpaEntity entity = new AuthUserJpaEntity();
        entity.setUserId(user.getUserId());
        entity.setEmail(user.getEmail());
        entity.setHashedPassword(user.getHashedPassword());
        entity.setRoles(user.getRoles());
        entity.setRefreshTokens(
                user.getRefreshTokens().stream()
                        .map(RefreshTokenJpaEntity::fromDomain)
                        .collect(Collectors.toSet())
        );
        entity.setActive(user.isActive());
        return entity;
    }
}