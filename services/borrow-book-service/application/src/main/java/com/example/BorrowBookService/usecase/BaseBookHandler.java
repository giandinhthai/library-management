package com.example.BorrowBookService.usecase;

import com.example.BorrowBookService.exception.InvalidBorrowRequestException;
import com.example.BorrowBookService.exception.InvalidReservationRequestException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class BaseBookHandler {
    protected <T extends RuntimeException> void checkForDuplicateBooks(
            List<UUID> bookUUIDs,
            Function<String, T> exceptionSupplier) {

        Set<UUID> uniqueBookIds = new HashSet<>();
        for (UUID bookUUID : bookUUIDs) {
            if (!uniqueBookIds.add(bookUUID)) {
                throw exceptionSupplier.apply("Some book are duplicate");
            }
        }
    }
}
