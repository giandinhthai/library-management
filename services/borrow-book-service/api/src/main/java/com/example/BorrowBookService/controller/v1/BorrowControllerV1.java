package com.example.BorrowBookService.controller.v1;

import com.example.BorrowBookService.DTO.book.BookResult;
import com.example.BorrowBookService.DTO.borrow.BorrowItemOnBook;
import com.example.BorrowBookService.DTO.borrow.BorrowResult;
import com.example.BorrowBookService.DTO.reverse.ReserveResult;
import com.example.BorrowBookService.aggregate.BorrowStatus;
import com.example.BorrowBookService.aggregate.ReservationStatus;
import com.example.BorrowBookService.usecase.command.BorrowBook;
import com.example.BorrowBookService.usecase.command.CompleteReservation;
import com.example.BorrowBookService.usecase.command.ReserveBook;
import com.example.BorrowBookService.usecase.command.ReturnBook;
import com.example.BorrowBookService.usecase.query.*;
import com.example.buildingblocks.cqrs.mediator.Mediator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.buildingblocks.shared.api.DTO.RestApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Controller for handling book borrowing, returning, and reservation functionalities.
 * Provides endpoints for members to borrow, return, reserve, and complete reservations for books.
 */
@RestController
@RequestMapping(value = "/api/borrows", headers = "X-API-VERSION=1")
@RequiredArgsConstructor
public class BorrowControllerV1 {

    private final Mediator mediator;

    @Operation(summary = "Borrow books for a member")
    @PostMapping
    public ResponseEntity<RestApiResponse<BorrowResult>> borrowBooks(
            @RequestBody @Valid BorrowBook command) {
        BorrowResult result = mediator.send(command);
        return ResponseEntity.ok(RestApiResponse.success(result));
    }

    @Operation(summary = "Return books for a member")
    @PostMapping("/returns")
    public ResponseEntity<RestApiResponse<Set<BorrowResult>>> returnBooks(
            @RequestBody @Valid ReturnBook command) {
        Set<BorrowResult> results = mediator.send(command);
        return ResponseEntity.ok(RestApiResponse.success(results));
    }

    @Operation(summary = "Get borrows for a member")
    @GetMapping("/members/{memberId}/borrows")
    public ResponseEntity<RestApiResponse<Page<BorrowResult>>> getMemberBorrows(
            @PathVariable UUID memberId,
            @RequestParam(required = false) BorrowStatus status,
            Pageable pageable) {
        var query = new GetMemberBorrows(memberId, status, pageable);
        Page<BorrowResult> results = mediator.send(query);
        return ResponseEntity.ok(RestApiResponse.success(results));
    }
}
//@RestController
//@RequestMapping(value = "/api/borrows", headers = "X-API-VERSION=1")
//@RequiredArgsConstructor
//public class BorrowControllerV1 {
//
//    private final Mediator mediator;
//
//    @Operation(summary = "Borrow books for a member", description = "Borrow a book for a member and return the result")
//    @ApiResponse(responseCode = "200", description = "Books successfully borrowed",
//            content = @Content(mediaType = "application/json",
//                    schema = @Schema(implementation = BorrowResult.class)))
//    @PostMapping
//    public ResponseEntity<RestApiResponse<BorrowResult>> borrowBooks(
//            @RequestBody @Valid BorrowBook command) {
//        BorrowResult result = mediator.send(command);
//        return ResponseEntity.ok(RestApiResponse.success(result));
//    }
//
//    @Operation(summary = "Return books for a member", description = "Return borrowed books for a member")
//    @ApiResponse(responseCode = "200", description = "Books successfully returned",
//            content = @Content(mediaType = "application/json",
//                    schema = @Schema(implementation = BorrowResult.class)))
//    @PostMapping("/returns")
//    public ResponseEntity<RestApiResponse<Set<BorrowResult>>> returnBooks(
//            @RequestBody @Valid ReturnBook command) {
//        Set<BorrowResult> results = mediator.send(command);
//        return ResponseEntity.ok(RestApiResponse.success(results));
//    }
//
//    @Operation(summary = "Reserve books for a member", description = "Reserve books for a member and return the result")
//    @ApiResponse(responseCode = "200", description = "Books successfully reserved",
//            content = @Content(mediaType = "application/json",
//                    schema = @Schema(implementation = ReserveResult.class)))
//    @PostMapping("/reservation")
//    public ResponseEntity<RestApiResponse<List<ReserveResult>>> reserveBooks(
//            @RequestBody @Valid ReserveBook command) {
//        List<ReserveResult> results = mediator.send(command);
//        return ResponseEntity.ok(RestApiResponse.success(results));
//    }
//
//    @Operation(summary = "Complete a reservation", description = "Complete a reservation for a member using reservation ID and member ID")
//    @ApiResponse(responseCode = "200", description = "Reservation successfully completed",
//            content = @Content(mediaType = "application/json",
//                    schema = @Schema(implementation = BorrowResult.class)))
//    @PostMapping("/reservation/{reservationId}/complete")
//    public ResponseEntity<RestApiResponse<BorrowResult>> completeReservation(
//           @RequestBody @Valid CompleteReservation command) {
//        BorrowResult result = mediator.send(command);
//        return ResponseEntity.ok(RestApiResponse.success(result));
//    }
//
//    @Operation(summary = "Get reservations for a member", description = "Retrieve a list of reservations for a specific member")
//    @ApiResponse(responseCode = "200", description = "List of member reservations",
//            content = @Content(mediaType = "application/json",
//                    schema = @Schema(implementation = ReserveResult.class)))
//    @GetMapping("/members/{memberId}/reservations")
//    public ResponseEntity<RestApiResponse<List<ReserveResult>>> getMemberReservations(
//            @PathVariable UUID memberId,
//            @RequestParam(required = false) ReservationStatus status) {
//        var query = new GetMemberReservations(memberId, status);
//        List<ReserveResult> results = mediator.send(query);
//        return ResponseEntity.ok(RestApiResponse.success(results));
//    }
//
//
//    @Operation(summary = "Get borrows for a member", description = "Retrieve a paginated list of books borrowed by a specific member")
//    @ApiResponse(responseCode = "200", description = "List of member borrowings",
//            content = @Content(mediaType = "application/json",
//                    schema = @Schema(implementation = BorrowResult.class)))
//    @GetMapping("/members/{memberId}/borrows")
//    public ResponseEntity<RestApiResponse<Page<BorrowResult>>> getMemberBorrows(
//            @PathVariable UUID memberId,
//            @RequestParam(required = false) BorrowStatus status,
//            Pageable pageable) {
//        var query = new GetMemberBorrows(memberId, status, pageable);
//        Page<BorrowResult> results = mediator.send(query);
//        return ResponseEntity.ok(RestApiResponse.success(results));
//    }
//
//
//    @Operation(summary = "Get a specific book", description = "Retrieve details of a specific book by its ID")
//    @ApiResponse(responseCode = "200", description = "Book details retrieved",
//            content = @Content(mediaType = "application/json",
//                    schema = @Schema(implementation = BookResult.class)))
//    @GetMapping("/{bookId}")
//    public ResponseEntity<RestApiResponse<BookResult>> getBook(
//            @PathVariable UUID bookId) {
//        var query = new GetBook(bookId);
//        BookResult result = mediator.send(query);
//        return ResponseEntity.ok(RestApiResponse.success(result));
//    }
//
//
//    @Operation(summary = "Get borrow details for a specific book", description = "Retrieve borrow information for a book by its ID")
//    @ApiResponse(responseCode = "200", description = "Borrow details of the book",
//            content = @Content(mediaType = "application/json",
//                    schema = @Schema(implementation = BorrowItemOnBook.class)))
//    @GetMapping("/{bookId}/borrows")
//    public ResponseEntity<RestApiResponse<Page<BorrowItemOnBook>>> getBorrowOnBook(
//            @PathVariable UUID bookId,
//            @RequestParam(required = false) Boolean isReturned,
//            Pageable pageable) {
//        var query = new GetBorrowOnBook(bookId, isReturned, pageable);
//        Page<BorrowItemOnBook> borrowResults = mediator.send(query);
//        return ResponseEntity.ok(RestApiResponse.success(borrowResults));
//    }
//
//
//    @Operation(summary = "Get list reservation details for a specific book", description = "Retrieve reservation information for a book by its ID")
//    @ApiResponse(responseCode = "200", description = "Reservation details of the book",
//            content = @Content(mediaType = "application/json",
//                    schema = @Schema(implementation = ReserveResult.class)))
//    @GetMapping("/{bookId}/reservations")
//    public ResponseEntity<RestApiResponse<Page<ReserveResult>>> getReservationOnBook(
//            @PathVariable UUID bookId,
//            @RequestParam(required = false) ReservationStatus status,
//            Pageable pageable) {
//        var query = new GetReservationOnBook(bookId,status, pageable);
//        Page<ReserveResult> borrowResults = mediator.send(query);
//        return ResponseEntity.ok(RestApiResponse.success(borrowResults));
//    }
//}
