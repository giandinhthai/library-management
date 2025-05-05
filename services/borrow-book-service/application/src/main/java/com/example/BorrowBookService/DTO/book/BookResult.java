package com.example.BorrowBookService.DTO.book;

import com.example.BorrowBookService.aggregate.BookStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BookResult {
    private UUID bookId;

    private int price;

    private int quantity;

    private int availableQuantity;

    private int reservationQuantity;

    private BookStatus status;
}
