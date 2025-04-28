package com.example.BorrowBookService.jpa.repository;

import com.example.BorrowBookService.aggregate.BorrowItem;
import com.example.BorrowBookService.aggregate.BorrowStatus;
import com.example.BorrowBookService.jpa.repository.jpa.JpaBorrowItemRepository;
import com.example.BorrowBookService.repository.BorrowItemReadOnlyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BorrowItemReadOnlyRepositoryAdapter implements BorrowItemReadOnlyRepository {

    private final JpaBorrowItemRepository jpaBorrowItemRepository;
    @Override
    public List<BorrowItem> getBorrowItem(UUID memberId, List<UUID> listBookId) {
        return jpaBorrowItemRepository.getBorrowItem(memberId,listBookId);
    }

    @Override
    public Page<BorrowItem> getBorrowItemOnBook(UUID bookId, Boolean isReturned, Pageable pageable) {
        return jpaBorrowItemRepository.getBorrowItemOnBook(bookId,isReturned,pageable);
    }
}
