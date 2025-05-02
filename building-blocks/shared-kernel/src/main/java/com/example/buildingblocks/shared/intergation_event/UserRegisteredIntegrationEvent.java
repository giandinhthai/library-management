package com.example.buildingblocks.shared.intergation_event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisteredIntegrationEvent implements IntegrationEvent {
    UUID userId;
    String email;
    String role;
    Instant occurredAt;

    @Override
    public String getEventName() {
        return "user.registered";
    }
    @Override
    public Instant getOccurredAt() {
        return occurredAt;
    }
}
