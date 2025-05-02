package com.example.BorrowBookService.event;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class BooksReturnedEvent {
    private final UUID eventId;
    private final List<UUID> bookIds;
    private final LocalDateTime occurredOn;

    public BooksReturnedEvent(List<UUID> bookIds) {
        this.eventId = UUID.randomUUID();
        this.bookIds = bookIds;
        this.occurredOn = LocalDateTime.now();
    }


    public String getEventType() {
        return "BookReturnedEvent";
    }
}
