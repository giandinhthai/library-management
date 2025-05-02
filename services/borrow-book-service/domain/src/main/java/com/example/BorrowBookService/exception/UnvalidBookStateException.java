package com.example.BorrowBookService.exception;

public class UnvalidBookStateException extends RuntimeException {
    public UnvalidBookStateException(String message) {
        super(message);
    }
}

