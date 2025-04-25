package com.example.BorrowBookService.jpa.repository.jpa;

import com.example.BorrowBookService.aggregate.Book;
import com.example.BorrowBookService.aggregate.ReservationStatus;
import com.example.BorrowBookService.DTO.BookAvailablePair;
import com.example.BorrowBookService.DTO.BookPricePair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface JpaBookRepository extends JpaRepository<Book, UUID> {

    @Query(value = """
        SELECT book_id AS bookId,
               (available_quantity > 0 AND status = 'ACTIVE') AS available
        FROM books
        WHERE book_id IN (:bookIds)
        """, nativeQuery = true)
    List<BookAvailablePair> checkBookAvailabilityForBorrow(@Param("bookIds") List<UUID> bookIds);

    @Query(value = """
        SELECT book_id AS bookId, price
        FROM books
        WHERE book_id IN (:bookIds)
        """, nativeQuery = true)
    List<BookPricePair> findBookPrices(@Param("bookIds") List<UUID> bookUUIDs);

    @Query(value = """ 
        SELECT book_id AS bookId,
               ( books.available_quantity = 0 AND status = 'ACTIVE') AS available
        FROM books
        WHERE book_id IN (:bookIds)
        """, nativeQuery = true)
    List<BookAvailablePair> checkBookAvailabilityForReserve(
            @Param("bookIds") List<UUID> bookIds
    );
    @Query(value = """
        SELECT price
        FROM books
        WHERE book_id = :bookId
        """, nativeQuery = true)
    Integer getPrice(@Param("bookId") UUID bookId);
}