package com.example.BorrowBookService.repository;

import com.example.BorrowBookService.aggregate.BorrowItem;

import java.util.List;
import java.util.UUID;

public interface BorrowItemReadOnlyRepository {

    List<BorrowItem> getBorrowItem(UUID memberId, List<UUID> listBookId);
}
