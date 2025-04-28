package com.example.BorrowBookService.jpa.mapper;

import com.example.BorrowBookService.DTO.book.BookResult;
import com.example.BorrowBookService.DTO.book.mapper.BookMapper;
import com.example.BorrowBookService.DTO.borrow.BorrowItemResult;
import com.example.BorrowBookService.DTO.borrow.BorrowResult;
import com.example.BorrowBookService.DTO.borrow.mapper.BorrowMapper;
import com.example.BorrowBookService.aggregate.Book;
import com.example.BorrowBookService.aggregate.BookStatus;
import com.example.BorrowBookService.aggregate.Borrow;
import com.example.BorrowBookService.aggregate.BorrowItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component("bookMapper")
public interface BookMapperAdapter extends BookMapper {

    @Override
    @Mapping(source = "status", target = "status", qualifiedByName = "mapStatus")
    @Mapping(source = "bookId", target = "bookId")
    BookResult toResult(Book book);


    @Named("mapStatus")
    default String mapStatus(BookStatus status) {
        return status.name();
    }
}