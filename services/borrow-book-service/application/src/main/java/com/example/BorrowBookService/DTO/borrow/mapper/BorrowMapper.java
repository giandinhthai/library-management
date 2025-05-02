package com.example.BorrowBookService.DTO.borrow.mapper;

import com.example.BorrowBookService.DTO.borrow.BorrowResult;
import com.example.BorrowBookService.aggregate.Borrow;

public interface BorrowMapper {
    BorrowResult toResult(Borrow borrow);
}
