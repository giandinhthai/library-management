package com.example.BorrowBookService.jpa.repository;

import com.example.BorrowBookService.aggregate.Reservation;
import com.example.BorrowBookService.aggregate.ReservationStatus;
import com.example.BorrowBookService.exception.NotFoundException;
import com.example.BorrowBookService.jpa.repository.jpa.JpaReservationRepository;
import com.example.BorrowBookService.repository.ReservationReadOnlyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReservationReadOnlyRepositoryAdapter implements ReservationReadOnlyRepository
{
    private final JpaReservationRepository jpaReservationRepository;

    @Override
    public List<Reservation> getNextReservationOnBook(UUID bookId,int limit) {
        return jpaReservationRepository.getNextReservationOnBook(bookId, limit);
    }

    @Override
    public Set<Reservation> getNextReservationsOnBooks(Set<UUID> bookIds) {
        return jpaReservationRepository.getNextReservationOnBooks(bookIds);
    }

    @Override
    public Reservation getReadyReservation(UUID memberId, UUID bookId) {
        return jpaReservationRepository.getReadyReservation(memberId, bookId);
    }

    @Override
    public Reservation getReservationByIdOrThrow(UUID reservationId) {
        return jpaReservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found with id: " + reservationId));
    }

    @Override
    public List<Reservation> getAllReservation(UUID memberId, ReservationStatus status) {
        return jpaReservationRepository.findByMemberIdAndStatus(memberId, status);
    }

    @Override
    public Page<Reservation> getReservationByBook(UUID bookId, ReservationStatus status, Pageable pageable) {
        return jpaReservationRepository.getReservationByBook(bookId, status, pageable);
    }
}
