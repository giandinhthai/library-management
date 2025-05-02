package com.example.authservice.event;


import com.example.authservice.aggregate.Role;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserCreatedEvent {
    private final UUID userId;
    private final String email;
    private final Role role;
    private final Instant occurredAt;

    public UserCreatedEvent(UUID userId, String email, Role role) {
        this.userId = userId;
        this.email = email;
        this.occurredAt = Instant.now();
        this.role= role;
    }

}