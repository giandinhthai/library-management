package com.example.BorrowBookService.repository;

import com.example.BorrowBookService.aggregate.Member;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository {
    Member findByIdOrThrow(UUID memberId);
    Member save(Member member);
}