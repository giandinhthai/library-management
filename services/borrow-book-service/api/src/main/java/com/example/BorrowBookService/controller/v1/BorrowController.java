package com.example.BorrowBookService.controller.v1;

import com.example.BorrowBookService.DTO.borrow.BorrowResult;
import com.example.BorrowBookService.usecase.BorrowBook;
import com.example.BorrowBookService.usecase.ReturnBook;
import com.example.buildingblocks.cqrs.mediator.Mediator;
import com.example.buildingblocks.cqrs.mediator.SpringMediator;
import com.example.buildingblocks.shared.api.DTO.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
@AllArgsConstructor
public class BorrowController {
    private final Mediator mediator;
    @PostMapping
    public ResponseEntity<ApiResponse<BorrowResult>> borrowBooks(
            @RequestBody @Valid BorrowBook  command) {
        BorrowResult result = mediator.send(command);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    @PostMapping("/returns")
    public ResponseEntity<ApiResponse<List<BorrowResult>>> returnBooks(
            @RequestBody @Valid ReturnBook command) {
        List<BorrowResult> results = mediator.send(command);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

}
