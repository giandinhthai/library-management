package com.example.authservice.jpa.entity;

import com.example.authservice.aggregate.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "auth_users")
@Getter
@Setter
public class AuthUserJpaEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    @Column(nullable = false,unique = true)
    private String email;

    private String hashedPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Set<RefreshTokenJpaEntity> refreshTokens = new HashSet<>();

    private boolean active;

}