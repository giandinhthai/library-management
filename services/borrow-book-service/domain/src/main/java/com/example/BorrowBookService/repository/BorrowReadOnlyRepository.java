package com.example.BorrowBookService.repository;

import com.example.BorrowBookService.aggregate.Borrow;
import com.example.BorrowBookService.aggregate.BorrowStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BorrowReadOnlyRepository {
    Page<Borrow> getBorrow(UUID memberId, BorrowStatus status, Pageable pageable);
}
