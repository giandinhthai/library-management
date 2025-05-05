package com.example.BorrowBookService.event;

import com.example.BorrowBookService.aggregate.Reservation;
import lombok.Getter;

import java.time.Instant;

/**
 * Event representing that a member has completed a reservation by borrowing the book.
 */
@Getter
public class MemberCompleteReservationEvent {
    private final Reservation reservation;
    private final Instant occurredAt;

    public MemberCompleteReservationEvent(Reservation reservation) {
        this.reservation = reservation;
        this.occurredAt = Instant.now();
    }
}
