package com.example.BorrowBookService.event;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
public class BooksReservedEvent {
    private final UUID eventId;
    private final UUID memberId;
    private final Set<UUID> bookIds;
    private final LocalDateTime occurredOn;
    public BooksReservedEvent(UUID memberId, Set<UUID> bookIds) {
        this.eventId = UUID.randomUUID();
        this.bookIds = bookIds;
        this.memberId = memberId;
        this.occurredOn = LocalDateTime.now();
    }

}
