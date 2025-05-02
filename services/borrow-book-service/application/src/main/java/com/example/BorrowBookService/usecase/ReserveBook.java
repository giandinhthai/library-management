package com.example.BorrowBookService.usecase;

import com.example.BorrowBookService.DTO.reverse.ReserveResult;
import com.example.buildingblocks.cqrs.request.Command;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReserveBook implements Command<ReserveResult> {
    @NotBlank(message = "Member id is required")
    private String memberId;
    @NotBlank(message = "Book id is required")
    private String bookId;
}
