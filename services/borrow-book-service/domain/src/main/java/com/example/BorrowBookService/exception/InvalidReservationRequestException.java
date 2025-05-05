package com.example.BorrowBookService.exception;

public class InvalidReservationRequestException extends RuntimeException {
    public InvalidReservationRequestException(String message) {
        super(message);
    }
    public String getMessage() {
        return super.getMessage();
    }
}

