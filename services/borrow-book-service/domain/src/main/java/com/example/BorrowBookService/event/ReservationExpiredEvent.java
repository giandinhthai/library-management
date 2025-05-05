package com.example.BorrowBookService.event;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
public class ReservationExpiredEvent {
    private final UUID memberId;
    private final UUID bookId;
    private final LocalDateTime occurredOn;

    public ReservationExpiredEvent(UUID memberId, UUID bookId) {
        this.memberId = memberId;
        this.bookId = bookId;
        this.occurredOn= LocalDateTime.now();
    }

}
