package com.example.BorrowBookService.DTO;

import java.util.UUID;

public interface BookPricePair {
    UUID getBookId();
    Integer getPrice();
}