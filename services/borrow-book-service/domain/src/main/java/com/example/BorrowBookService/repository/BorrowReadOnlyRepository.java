package com.example.BorrowBookService.repository;

import com.example.BorrowBookService.aggregate.Borrow;
import com.example.BorrowBookService.aggregate.BorrowStatus;

import java.util.List;
import java.util.UUID;

public interface BorrowReadOnlyRepository {
    List<Borrow> getBorrow(UUID memberId, BorrowStatus status);
}
