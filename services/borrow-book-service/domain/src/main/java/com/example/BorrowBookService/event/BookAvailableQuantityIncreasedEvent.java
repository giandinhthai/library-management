package com.example.BorrowBookService.event;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * Event representing that the available quantity of a book has increased.
 */
@Getter
public class BookAvailableQuantityIncreasedEvent {
    private final UUID bookId;
    private final Instant occurredAt;

    public BookAvailableQuantityIncreasedEvent(UUID bookId) {
        this.bookId = bookId;
        this.occurredAt = Instant.now();
    }

}
