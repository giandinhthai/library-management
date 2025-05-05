package com.example.BorrowBookService.repository;

import com.example.BorrowBookService.aggregate.Reservation;
import com.example.BorrowBookService.aggregate.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ReservationReadOnlyRepository {

    Reservation getNextReservationOnBook(UUID bookId);
    Set<Reservation> getNextReservationsOnBooks(Set<UUID> bookIds);

    Reservation getReadyReservation(UUID memberId, UUID bookId);
    Reservation getReservationByIdOrThrow(UUID reservationId);

    List<Reservation> getAllReservation(UUID memberId, ReservationStatus status);

    Page<Reservation> getReservationByBook(UUID bookId, ReservationStatus status, Pageable pageable);
}