package com.example.BorrowBookService.usecase.query;

import com.example.BorrowBookService.DTO.book.BookResult;
import com.example.BorrowBookService.DTO.book.mapper.BookMapper;
import com.example.BorrowBookService.repository.BookRepository;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class GetBook implements Query<BookResult> {
    private UUID bookId;
}
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class GetBookHandler implements RequestHandler<GetBook,BookResult>{
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    @Override
    public BookResult handle(GetBook request) {
        return bookMapper.toResult(bookRepository.findByIdOrThrow(request.getBookId()));

    }
}