package com.example.BorrowBookService.repository;

import com.example.BorrowBookService.aggregate.BorrowItem;
import com.example.BorrowBookService.aggregate.BorrowStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BorrowItemReadOnlyRepository {

    List<BorrowItem> getBorrowItem(UUID memberId, List<UUID> listBookId);

    Page<BorrowItem> getBorrowItemOnBook(UUID bookId, Boolean isReturned, Pageable pageable);
}
