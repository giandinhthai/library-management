package com.example.BorrowBookService.DTO.book.mapper;

import com.example.BorrowBookService.DTO.book.BookResult;
import com.example.BorrowBookService.aggregate.Book;

public interface BookMapper {
    BookResult toResult(Book book);
}
