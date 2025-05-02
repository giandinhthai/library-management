package com.example.buildingblocks.shared.domain.event;

import java.time.LocalDateTime;

public interface DomainEvent {
    LocalDateTime getOccurredOn();
    String getEventType();
}
