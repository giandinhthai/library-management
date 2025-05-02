package com.example.BorrowBookService.exception;

public class UnexpectedBorrowStateException extends RuntimeException {
    public UnexpectedBorrowStateException(String message) {
        super(message);
    }
}

