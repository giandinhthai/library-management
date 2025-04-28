package com.example.BorrowBookService.jpa.repository.jpa;

import com.example.BorrowBookService.aggregate.Book;
import com.example.BorrowBookService.aggregate.BorrowItem;
import com.example.BorrowBookService.aggregate.BorrowStatus;
import com.example.BorrowBookService.aggregate.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaBorrowItemRepository extends JpaRepository<BorrowItem, UUID> {
    @Query("""
            SELECT bi
            FROM Borrow b
            JOIN b.borrowItems bi
            WHERE b.member.memberId = :memberId
            AND bi.bookId IN :listBookId
            AND bi.returned = false
            """)
    List<BorrowItem> getBorrowItem(@Param("memberId") UUID memberId,@Param("listBookId") List<UUID> listBookId);
    @Query("""
            SELECT bi
            FROM BorrowItem bi
            WHERE bi.bookId = :bookId
            AND (:isReturned is null or bi.returned = :isReturned)
            """)
    Page<BorrowItem> getBorrowItemOnBook(@Param("bookId") UUID bookId,@Param("isReturned") Boolean isReturned, Pageable pageable);
}
