package com.example.BorrowBookService.exception;

public class InvalidBookStateException extends RuntimeException {
    public InvalidBookStateException(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }
}

