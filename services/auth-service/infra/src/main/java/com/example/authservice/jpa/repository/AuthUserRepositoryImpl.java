package com.example.authservice.jpa.repository;


import com.example.authservice.aggregate.AuthUser;
import com.example.authservice.jpa.entity.AuthUserJpaEntity;
import com.example.authservice.jpa.mapper.AuthUserMapper;
import com.example.authservice.jpa.repository.jpa.JpaAuthUserRepository;
import com.example.authservice.repositories.AuthUserRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public class AuthUserRepositoryImpl implements AuthUserRepository {

    private final JpaAuthUserRepository jpaAuthUserRepository;

    public AuthUserRepositoryImpl(JpaAuthUserRepository jpaAuthUserRepository) {
        this.jpaAuthUserRepository = jpaAuthUserRepository;
    }


    @Override
    public Optional<AuthUser> findByEmail(String email) {
        return jpaAuthUserRepository.findByEmail(email)
                .map(AuthUserMapper::toDomain);
    }

    @Override
    public Optional<AuthUser> findByUserId(UUID userId) {
        return jpaAuthUserRepository.findById(userId)
                .map(AuthUserMapper::toDomain);
    }

    @Override
    public void save(AuthUser authUser) {
        AuthUserJpaEntity entity = AuthUserMapper.toJpa(authUser);
        jpaAuthUserRepository.save(entity);
    }
    @Override
    public Optional<AuthUser> findByRefreshToken(String refreshToken) {
        return jpaAuthUserRepository.findByRefreshToken(refreshToken)
                .map(AuthUserMapper::toDomain);
    }
}