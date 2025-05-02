package com.example.BorrowBookService.DTO.borrow;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class BorrowResult {
    private UUID borrowId;
    private UUID memberId;
    private LocalDateTime borrowedAt;
    private LocalDateTime dueDate;
    private String status;
    private int totalFineAmount;
    private boolean finePaid;
    private List<BorrowItemResult> borrowItems;
}
