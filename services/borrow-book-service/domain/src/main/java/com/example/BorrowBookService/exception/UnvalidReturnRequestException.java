package com.example.BorrowBookService.exception;

public class UnvalidReturnRequestException extends RuntimeException {
    public UnvalidReturnRequestException(String message) {
        super(message);
    }
}

