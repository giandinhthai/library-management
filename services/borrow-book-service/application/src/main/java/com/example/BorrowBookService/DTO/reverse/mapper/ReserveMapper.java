package com.example.BorrowBookService.DTO.reverse.mapper;

import com.example.BorrowBookService.DTO.borrow.BorrowResult;
import com.example.BorrowBookService.DTO.reverse.ReserveResult;
import com.example.BorrowBookService.aggregate.Borrow;
import com.example.BorrowBookService.aggregate.Reservation;

public interface ReserveMapper {
    ReserveResult toResult(Reservation reservation);
}
