package com.example.BorrowBookService.jpa.repository;

import com.example.BorrowBookService.aggregate.Borrow;
import com.example.BorrowBookService.aggregate.BorrowStatus;
import com.example.BorrowBookService.jpa.repository.jpa.JpaBorrowRepository;
import com.example.BorrowBookService.repository.BorrowReadOnlyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@RequiredArgsConstructor
@Repository
public class BorrowReadOnlyRepositoryAdapter implements BorrowReadOnlyRepository {
    private final JpaBorrowRepository jpaBorrowRepository;
    @Override
    public Page<Borrow> getBorrow(UUID memberId, BorrowStatus status, Pageable pageable) {
        return jpaBorrowRepository.getBorrowBy(memberId,status, pageable);
    }
}
