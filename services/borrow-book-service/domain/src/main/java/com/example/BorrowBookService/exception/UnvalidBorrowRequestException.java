package com.example.BorrowBookService.exception;

public class UnvalidBorrowRequestException extends RuntimeException {
    public UnvalidBorrowRequestException(String message) {
        super(message);
    }
}