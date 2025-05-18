package com.example.BorrowBookService.controller.v1;

import com.example.BorrowBookService.DTO.book.BookResult;
import com.example.BorrowBookService.DTO.borrow.BorrowItemOnBook;
import com.example.BorrowBookService.DTO.reverse.ReserveResult;
import com.example.BorrowBookService.aggregate.ReservationStatus;
import com.example.BorrowBookService.usecase.command.book.GetBooks;
import com.example.BorrowBookService.usecase.query.book.GetBook;
import com.example.BorrowBookService.usecase.query.book.GetBorrowOnBook;
import com.example.BorrowBookService.usecase.query.book.GetReservationOnBook;
import com.example.buildingblocks.cqrs.mediator.Mediator;
import com.example.buildingblocks.shared.api.DTO.RestApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(value = "/api/books", headers = "X-API-VERSION=1")
@RequiredArgsConstructor
public class BookControllerV1 {

    private final Mediator mediator;

    @Operation(summary = "Get a specific book")
    @GetMapping("/{bookId}")
    public ResponseEntity<RestApiResponse<BookResult>> getBook(
            @PathVariable UUID bookId) {
        var query = new GetBook(bookId);
        BookResult result = mediator.send(query);
        return ResponseEntity.ok(RestApiResponse.success(result));
    }

    @Operation(summary = "Get borrow details for a specific book")
    @GetMapping("/{bookId}/borrows")
    public ResponseEntity<RestApiResponse<Page<BorrowItemOnBook>>> getBorrowOnBook(
            @PathVariable UUID bookId,
            @RequestParam(required = false) Boolean isReturned,
            Pageable pageable) {
        var query = new GetBorrowOnBook(bookId, isReturned, pageable);
        Page<BorrowItemOnBook> borrowResults = mediator.send(query);
        return ResponseEntity.ok(RestApiResponse.success(borrowResults));
    }

    @Operation(summary = "Get reservation details for a specific book")
    @GetMapping("/{bookId}/reservations")
    public ResponseEntity<RestApiResponse<Page<ReserveResult>>> getReservationOnBook(
            @PathVariable UUID bookId,
            @RequestParam(required = false) ReservationStatus status,
            Pageable pageable) {
        var query = new GetReservationOnBook(bookId, status, pageable);
        Page<ReserveResult> results = mediator.send(query);
        return ResponseEntity.ok(RestApiResponse.success(results));
    }
    @Operation(summary = "Get multiple books by IDs")
    @GetMapping
    public ResponseEntity<RestApiResponse<List<BookResult>>> getBooks(
            @RequestParam List<UUID> bookIds) {
        var query = new GetBooks(bookIds);
        List<BookResult> results = mediator.send(query);
        return ResponseEntity.ok(RestApiResponse.success(results));
    }
}
