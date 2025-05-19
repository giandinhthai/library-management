package com.example.buildingblocks.shared.intergation_event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BorrowDueSoonIntegrationEvent implements IntegrationEvent {
    UUID memberId;
    UUID borrowId;
    Instant occurredAt=Instant.now();;
    @Override
    public String getEventName() {
        return "borrow.due";
    }
    @Override
    public String getAggregateId() {
        return memberId.toString();
    }


}
