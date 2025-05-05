package com.example.BorrowBookService.DTO;

import java.util.UUID;

public interface BookAvailablePair {
    UUID getBookId();
    Boolean getAvailable();
}