package com.example.BorrowBookService.jpa.repository.jpa;

import com.example.BorrowBookService.aggregate.Book;
import com.example.BorrowBookService.aggregate.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaBookRepository extends JpaRepository<Book, UUID> {
    @Query("SELECT b.bookId, (b.availableQuantity > 0 AND b.status = 'ACTIVE') FROM Book b WHERE b.bookId IN :bookIds")
    List<Object[]> checkBookAvailability(@Param("bookIds") List<UUID> bookIds);
    @Query("SELECT b.bookId, b.price FROM Book b WHERE b.bookId IN :bookIds")
    List<Object[]> findBookPrices(@Param("bookIds") List<UUID> bookUUIDs);

    @Query("""
    SELECT b.bookId,
           CASE 
               WHEN b.status = 'ACTIVE' AND b.availableQuantity > 
                    (SELECT COUNT(r) 
                     FROM Reservation r 
                     WHERE r.bookId = b.bookId 
                       AND r.status IN :availableStatuses) 
               THEN TRUE 
               ELSE FALSE 
           END
    FROM Book b 
    WHERE b.bookId IN :bookIds
    """)
    List<Object[]> checkBookAvailabilityForReserve(
            @Param("bookIds") List<UUID> bookIds,
            @Param("availableStatuses") List<ReservationStatus> availableStatuses
    );
}