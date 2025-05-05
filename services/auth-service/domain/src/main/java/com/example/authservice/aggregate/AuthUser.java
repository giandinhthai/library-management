package com.example.authservice.aggregate;

import com.example.authservice.exceptions.InvalidTokenException;
import com.example.authservice.service.TokenGenerator;
import com.example.buildingblocks.security.encoder.PasswordEncoder;
import com.example.authservice.event.UserCreatedEvent;
import lombok.Getter;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@Getter
public class AuthUser extends AbstractAggregateRoot<AuthUser>  {

    private final UUID userId;

    private String email;

    private String hashedPassword;

    private Role role;
    private final Set<RefreshToken> refreshTokens = new HashSet<>();

    private boolean active = true;

    private AuthUser(UUID userId, String email, String hashedPassword, Role role) {
        this.userId = userId;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.role = role;
        registerEvent(new UserCreatedEvent(userId, email, role));
    }

    // Factory method for creating a new user
    public static AuthUser create(String email, String password, Role role, PasswordEncoder passwordEncoder) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }

        if (passwordEncoder == null) {
            throw new IllegalArgumentException("Password encoder cannot be null");
        }

        UUID userId = UUID.randomUUID();
        String hashedPassword = passwordEncoder.encode(password);
        return new AuthUser(userId, email, hashedPassword, role);
    }

    public static AuthUser reconstitute(UUID userId, String email, String hashedPassword, Role role) {
        return new AuthUser(userId, email, hashedPassword, role);
    }

    private static boolean isValidEmail(String email) {
        // Basic email validation
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public boolean checkPassword(String rawPassword, PasswordEncoder encoder) {
        if (rawPassword == null || rawPassword.isBlank()) {
            return false;
        }

        boolean matches = encoder.matches(rawPassword, this.hashedPassword);
        return matches;
    }

    public void changeRole(Role newRole) {
        if (newRole == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        this.role = newRole;
    }

    public void deactivate() {
        this.active = false;
    }

    public void addRefreshToken(RefreshToken token) {
        if (token == null) {
            throw new IllegalArgumentException("Token cannot be null");
        }
        refreshTokens.add(token);
    }

    public void revokeAllTokens() {
        refreshTokens.forEach(RefreshToken::revoke);
    }

    public void changeEmail(String newEmail) {
        if (newEmail == null || newEmail.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (!isValidEmail(newEmail)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        this.email = newEmail;
    }

    public void changePassword(String newHashedPassword) {
        if (newHashedPassword == null || newHashedPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        this.hashedPassword = newHashedPassword;
    }

    public Set<RefreshToken> getRefreshTokens() {
        return Collections.unmodifiableSet(refreshTokens);
    }

    public RefreshToken generateRefreshToken(TokenGenerator generator) {
        RefreshToken refreshToken = generator.createRefreshToken(userId);
        this.addRefreshToken(refreshToken);
        return refreshToken;
    }

    public void validateAndRevokeRefreshToken(String tokenToValidate) {
        // Find the token
        RefreshToken existingToken = refreshTokens.stream()
                .filter(token -> token.getToken().equals(tokenToValidate))
                .findFirst()
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));

        // Check if token is valid
        if (existingToken.isExpired()) {
            throw new InvalidTokenException("Refresh token is expired");
        }

        if (existingToken.isRevoked()) {
            throw new InvalidTokenException("Refresh token has been revoked");
        }
        existingToken.revoke();
    }
}