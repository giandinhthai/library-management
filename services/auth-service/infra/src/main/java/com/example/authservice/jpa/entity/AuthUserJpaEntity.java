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

    private String email;

    private String hashedPassword;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "auth_user_roles", joinColumns = @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_auth_user_roles"), nullable = false),
            foreignKey = @ForeignKey(name = "fk_auth_user_roles"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Set<RefreshTokenJpaEntity> refreshTokens = new HashSet<>();

    private boolean active;

}