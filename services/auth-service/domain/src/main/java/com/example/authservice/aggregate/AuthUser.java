package com.example.authservice.aggregate;

import com.example.authservice.event.UserCreatedEvent;
import com.example.authservice.event.UserLoggedInEvent;
import com.example.authservice.exceptions.InvalidTokenException;
import com.example.authservice.service.TokenGenerator;
import com.example.buildingblocks.security.encoder.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AuthUser {
    private final UUID userId;
    private String email;
    private String hashedPassword;
    private final Set<Role> roles = new HashSet<>();
    private final Set<RefreshToken> refreshTokens = new HashSet<>();
    private boolean active = true;
    private final Set<Object> domainEvents = new HashSet<>();

    private AuthUser(UUID userId, String email, String hashedPassword) {
        this.userId = userId;
        this.email = email;
        this.hashedPassword = hashedPassword;
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
        AuthUser user = new AuthUser(userId, email, hashedPassword);
        user.addRole(role);
        user.addDomainEvent(new UserCreatedEvent(userId, email));
        return user;
    }
    public static AuthUser reconstitute(UUID userId, String email, String hashedPassword) {
        return new AuthUser(userId, email, hashedPassword);
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
        if (matches) {
            addDomainEvent(new UserLoggedInEvent(userId, email));
        }
        return matches;
    }
    
    public void addRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        roles.add(role);
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

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public boolean isActive() {
        return active;
    }

    public Set<RefreshToken> getRefreshTokens() {
        return Collections.unmodifiableSet(refreshTokens);
    }
    
    public RefreshToken generateRefreshToken(TokenGenerator generator) {
        RefreshToken refreshToken = generator.createRefreshToken(userId);
        this.addRefreshToken(refreshToken);
        return refreshToken;
    }
    
    private void addDomainEvent(Object event) {
        domainEvents.add(event);
    }
    
    public Set<Object> getDomainEvents() {
        return Collections.unmodifiableSet(domainEvents);
    }
    
    public void clearDomainEvents() {
        domainEvents.clear();
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