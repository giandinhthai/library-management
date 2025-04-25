package com.example.BorrowBookService.repository;

import com.example.BorrowBookService.aggregate.Reservation;
import com.example.BorrowBookService.aggregate.ReservationStatus;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ReservationReadOnlyRepository {

    Reservation getNextReservationOnBook(UUID bookId);
    Set<Reservation> getNextReservationsOnBooks(Set<UUID> bookIds);

    Reservation getReadyReservation(UUID memberId, UUID bookId);
    Reservation getReservationByIdOrThrow(UUID reservationId);

    List<Reservation> getAllReservation(UUID memberId, ReservationStatus status);

    default List<Reservation> getAllReservation(UUID memberId) {
        return getAllReservation(memberId, null);
    }
}