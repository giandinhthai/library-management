package com.example.BorrowBookService.exception;

public class InvalidReturnRequestException extends RuntimeException {
    public InvalidReturnRequestException(String message) {
        super(message);
    }
    public String getMessage() {
        return super.getMessage();
    }
}

