package com.example.BorrowBookService.event;

import com.example.buildingblocks.shared.domain.event.DomainEvent;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class FineAmountUpdatedEvent   {
    private UUID eventId;
    private LocalDateTime occurredOn;
    private int amount;
    private UUID memberId;

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