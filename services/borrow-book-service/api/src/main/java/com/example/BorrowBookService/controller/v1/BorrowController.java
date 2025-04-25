package com.example.BorrowBookService.controller.v1;

import com.example.BorrowBookService.DTO.borrow.BorrowResult;
import com.example.BorrowBookService.DTO.reverse.ReserveResult;
import com.example.BorrowBookService.aggregate.ReservationStatus;
import com.example.BorrowBookService.usecase.command.BorrowBook;
import com.example.BorrowBookService.usecase.command.CompleteReservation;
import com.example.BorrowBookService.usecase.command.ReserveBook;
import com.example.BorrowBookService.usecase.command.ReturnBook;
import com.example.BorrowBookService.usecase.query.GetMemberReservations;
import com.example.buildingblocks.cqrs.mediator.Mediator;
import com.example.buildingblocks.shared.api.DTO.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/loans")
@AllArgsConstructor
public class BorrowController {
    private final Mediator mediator;

    @PostMapping
    public ResponseEntity<ApiResponse<BorrowResult>> borrowBooks(
            @RequestBody @Valid BorrowBook command) {
        BorrowResult result = mediator.send(command);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/returns")
    public ResponseEntity<ApiResponse<Set<BorrowResult>>> returnBooks(
            @RequestBody @Valid ReturnBook command) {
        Set<BorrowResult> results = mediator.send(command);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    @PostMapping("/reservation")
    public ResponseEntity<ApiResponse<List<ReserveResult>>> reserveBooks(
            @RequestBody @Valid ReserveBook command) {
        List<ReserveResult> results = mediator.send(command);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    @PostMapping("/reservation/{reservationId}/complete")
    public ResponseEntity<ApiResponse<BorrowResult>> completeReservation(
            @PathVariable UUID reservationId,
            @RequestBody @Valid UUID memberId) {
        CompleteReservation command = new CompleteReservation(reservationId,memberId);
        BorrowResult result = mediator.send(command);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/members/{memberId}/reservations")
    public ResponseEntity<ApiResponse<List<ReserveResult>>> getMemberReservations(
            @PathVariable UUID memberId,
            @RequestParam(required = false) ReservationStatus status) {
        var query = new GetMemberReservations(memberId, status);
        List<ReserveResult> results = mediator.send(query);
        return ResponseEntity.ok(ApiResponse.success(results));
    }
}
