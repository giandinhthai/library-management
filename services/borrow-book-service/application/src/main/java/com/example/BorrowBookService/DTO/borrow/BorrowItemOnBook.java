package com.example.BorrowBookService.DTO.borrow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
public class BorrowItemOnBook {
    private UUID borrowItemId;
    private UUID bookId;
    private UUID borrowId;
    private int bookPrice;
    private boolean returned;
    private LocalDateTime returnedAt;
    private int fineAmount;
    private UUID memberId;
    private LocalDateTime borrowAt;
    private LocalDateTime dueDate;
}
