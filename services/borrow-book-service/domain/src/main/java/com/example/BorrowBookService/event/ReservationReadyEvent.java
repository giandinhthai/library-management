package com.example.BorrowBookService.event;

import com.example.BorrowBookService.aggregate.Reservation;
import lombok.Getter;

import java.time.Instant;

/**
 * Event representing that a reservation is ready for the member to borrow.
 */
@Getter
public class ReservationReadyEvent {
    private final Reservation reservation;
    private final Instant occurredAt;

    public ReservationReadyEvent(Reservation reservation) {
        this.reservation = reservation;
        this.occurredAt = Instant.now();
    }
}
