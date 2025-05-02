package com.example.BorrowBookService.DTO.borrow;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BorrowItemResult {
    private UUID borrowItemId;
    private UUID bookId;
    private int bookPrice;
    private boolean returned;
    private LocalDateTime returnedAt;
    private int fineAmount;
}
