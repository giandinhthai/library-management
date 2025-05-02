package com.example.BorrowBookService.jpa.repository.jpa;

import com.example.BorrowBookService.aggregate.Book;
import com.example.BorrowBookService.aggregate.BorrowItem;
import com.example.BorrowBookService.aggregate.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaBorrowItemRepository extends JpaRepository<BorrowItem, UUID> {
}
