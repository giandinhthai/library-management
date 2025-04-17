package com.example.BorrowBookService.event;

import com.example.buildingblocks.shared.domain.event.DomainEvent;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class FineAmountUpdatedEvent   {
    private final UUID eventId;
    private final LocalDateTime occurredOn;
    private final int amount;
    private final UUID memberId;

    public FineAmountUpdatedEvent(int amount, UUID memberId) {
        this.eventId = UUID.randomUUID();
        this.occurredOn = LocalDateTime.now();
        this.amount = amount;
        this.memberId = memberId;
    }





    public String getEventType() {
        return "FineAmountUpdatedEvent";
    }

}