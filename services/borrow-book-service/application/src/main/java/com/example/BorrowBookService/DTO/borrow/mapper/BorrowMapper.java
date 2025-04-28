package com.example.BorrowBookService.DTO.borrow.mapper;

import com.example.BorrowBookService.DTO.borrow.BorrowItemOnBook;
import com.example.BorrowBookService.DTO.borrow.BorrowItemResult;
import com.example.BorrowBookService.DTO.borrow.BorrowResult;
import com.example.BorrowBookService.aggregate.Borrow;
import com.example.BorrowBookService.aggregate.BorrowItem;

public interface BorrowMapper {
    BorrowResult toResult(Borrow borrow);
    BorrowItemOnBook toResultOnBook(BorrowItem borrowItem);
}
