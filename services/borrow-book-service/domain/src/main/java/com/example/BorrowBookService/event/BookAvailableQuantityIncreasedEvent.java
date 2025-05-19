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
    private final int quantity;
    private final Instant occurredAt;

    public BookAvailableQuantityIncreasedEvent(UUID bookId, int quantity) {

        this.bookId = bookId;
        this.quantity = quantity;
        this.occurredAt = Instant.now();
    }

}
