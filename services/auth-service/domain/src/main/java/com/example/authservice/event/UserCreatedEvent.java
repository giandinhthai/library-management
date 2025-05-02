package com.example.authservice.event;


import java.time.Instant;
import java.util.UUID;

public class UserCreatedEvent {
    private final UUID userId;
    private final String email;
    private final Instant occurredAt;

    public UserCreatedEvent(UUID userId, String email) {
        this.userId = userId;
        this.email = email;
        this.occurredAt = Instant.now();
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}