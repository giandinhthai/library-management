package com.example.BorrowBookService.exception;

public class UnvalidReservationRequestException extends RuntimeException {
    public UnvalidReservationRequestException(String message) {
        super(message);
    }
}

