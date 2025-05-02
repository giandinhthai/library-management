package com.example.BorrowBookService.controller.v1;

import com.example.BorrowBookService.DTO.borrow.BorrowResult;
import com.example.BorrowBookService.DTO.reverse.ReserveResult;
import com.example.BorrowBookService.aggregate.ReservationStatus;
import com.example.BorrowBookService.usecase.command.CompleteReservation;
import com.example.BorrowBookService.usecase.command.ReserveBook;
import com.example.BorrowBookService.usecase.query.GetMemberReservations;
import com.example.buildingblocks.cqrs.mediator.Mediator;
import com.example.buildingblocks.shared.api.DTO.RestApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping(value = "/api/reservations", headers = "X-API-VERSION=1")
@RequiredArgsConstructor
public class ReservationControllerV1 {

    private final Mediator mediator;

    @Operation(summary = "Reserve books for a member")
    @PostMapping
    public ResponseEntity<RestApiResponse<List<ReserveResult>>> reserveBooks(
            @RequestBody @Valid ReserveBook command) {
        List<ReserveResult> results = mediator.send(command);
        return ResponseEntity.ok(RestApiResponse.success(results));
    }

    @Operation(summary = "Complete a reservation")
    @PostMapping("/{reservationId}/complete")
    public ResponseEntity<RestApiResponse<BorrowResult>> completeReservation(
            @RequestBody @Valid CompleteReservation command) {
        BorrowResult result = mediator.send(command);
        return ResponseEntity.ok(RestApiResponse.success(result));
    }

    @Operation(summary = "Get reservations for a member")
    @GetMapping("/members/{memberId}")
    public ResponseEntity<RestApiResponse<List<ReserveResult>>> getMemberReservations(
            @PathVariable UUID memberId,
            @RequestParam(required = false) ReservationStatus status) {
        var query = new GetMemberReservations(memberId, status);
        List<ReserveResult> results = mediator.send(query);
        return ResponseEntity.ok(RestApiResponse.success(results));
    }
}