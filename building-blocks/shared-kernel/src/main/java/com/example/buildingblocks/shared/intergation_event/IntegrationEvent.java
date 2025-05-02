package com.example.buildingblocks.shared.intergation_event;

import java.time.Instant;

public interface IntegrationEvent {
    String getEventName();
    Instant getOccurredAt();
}
