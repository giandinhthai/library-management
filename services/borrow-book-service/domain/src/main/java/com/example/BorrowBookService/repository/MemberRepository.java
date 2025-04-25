package com.example.BorrowBookService.repository;

import com.example.BorrowBookService.aggregate.Member;

import java.util.*;

public interface MemberRepository {
    Member findByIdOrThrow(UUID memberId);
    Member save(Member member);

    /**
     * Return map where key is member id and value is list of book id that member
     * has reservation
     */
//    Map<UUID, Set<UUID>> getNextReservationMember(List<UUID> bookIds);

    List<Member> saveAll(List<Member> members);

    List<Member> findAllWithReadyReservations();

//    Member getNextReservationMember(UUID bookId);
}