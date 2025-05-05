package com.example.BorrowBookService.exception;

import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Getter
public class InvalidBorrowRequestException extends RuntimeException {
    private final Set<UUID> unavailableBookIds;

    public InvalidBorrowRequestException(String message) {
        super(message);
        this.unavailableBookIds = null;
    }

    public InvalidBorrowRequestException(String message, Set<UUID> unavailableBookIds) {
        super(message);
        this.unavailableBookIds = unavailableBookIds;
    }

    @Override
    public String getMessage() {
        if (unavailableBookIds == null || unavailableBookIds.isEmpty()) {
            return super.getMessage();
        } else {
            return super.getMessage() + ", unavailable book IDs: " + unavailableBookIds;
        }
    }
}