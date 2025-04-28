package com.example.BorrowBookService.jpa.repository.jpa;

import com.example.BorrowBookService.aggregate.Borrow;
import com.example.BorrowBookService.aggregate.BorrowStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaBorrowRepository extends JpaRepository<Borrow, UUID> {
    @Query(value = """
            SELECT b
            FROM Borrow b
            WHERE b.member.memberId = :memberId
            AND (:status IS NULL OR b.status = :status)
            """)
    Page<Borrow> getBorrowOnMember(@Param("memberId") UUID memberId, @Param("status") BorrowStatus status, Pageable pageable);
}