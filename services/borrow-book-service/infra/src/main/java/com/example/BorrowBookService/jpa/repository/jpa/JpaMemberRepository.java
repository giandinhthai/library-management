package com.example.BorrowBookService.jpa.repository.jpa;

import com.example.BorrowBookService.aggregate.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaMemberRepository extends JpaRepository<Member, UUID> {
    @Query("SELECT DISTINCT m FROM Member m " +
            "JOIN FETCH m.reservations r " +
            "   WHERE r.status = 'READY_FOR_PICKUP'")
    List<Member> findAllWithReadyReservations();
}