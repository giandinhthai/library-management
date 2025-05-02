package com.example.BorrowBookService.DTO.reverse;

import com.example.BorrowBookService.aggregate.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ReserveResult {
    private UUID reserveId;
    private UUID bookId;
    private UUID memberId;
    private String status;
    private LocalDateTime reservedAt;
    private LocalDateTime expiresAt;
}
