package com.example.BorrowBookService.jpa.repository.jpa;

import com.example.BorrowBookService.aggregate.Reservation;
import com.example.BorrowBookService.aggregate.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface JpaReservationRepository extends JpaRepository<Reservation, UUID> {
    @Query(value = """
                SELECT r
                FROM Reservation r
                WHERE r.bookId = :bookId
                AND r.status = 'PENDING'
                order by r.reservedAt asc
                limit 1
            """)
    Reservation getNextReservationOnBook(@Param("bookId") UUID bookId);

    @Query(value = """
            SELECT *
            FROM (
                SELECT r.*,
                       ROW_NUMBER() OVER (PARTITION BY r.book_id ORDER BY r.reserved_at ASC, r.reservation_id ASC) as rn
                FROM reservations r
                WHERE r.status = 'PENDING'
                  AND r.book_id IN (:bookIds)
            ) t
            WHERE t.rn = 1
            """, nativeQuery = true)
    Set<Reservation> getNextReservationOnBooks(@Param("bookIds") Set<UUID> bookIds);
    @Query(value = """
            SELECT *
            FROM reservations r
            WHERE r.member_id = :memberId
            AND r.book_id = :bookId
            AND r.status = 'READY_FOR_PICKUP'
            """, nativeQuery = true)
    Reservation getReadyReservation(UUID memberId, UUID bookId);
    @Query(value = """
            SELECT r
            FROM Reservation r
            WHERE r.member.memberId = :memberId
            AND (:status IS NULL OR r.status = :status)
            ORDER BY r.reservedAt DESC
            """)
    List<Reservation> findByMemberIdAndStatus(@Param("memberId") UUID memberId, @Param("status") ReservationStatus status);
    @Query(value = """
            SELECT r
            FROM Reservation r
            WHERE r.bookId = :bookId
            AND (:status IS NULL OR r.status = :status)
            """)
    Page<Reservation> getReservationByBook(@Param("bookId") UUID bookId, @Param("status") ReservationStatus status, Pageable pageable);
}
