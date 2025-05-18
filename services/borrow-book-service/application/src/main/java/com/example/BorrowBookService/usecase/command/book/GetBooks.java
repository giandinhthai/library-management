package com.example.BorrowBookService.usecase.command.book;

import com.example.BorrowBookService.DTO.book.BookResult;
import com.example.BorrowBookService.DTO.book.mapper.BookMapper;
import com.example.BorrowBookService.repository.BookRepository;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Query;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class GetBooks implements Query<List<BookResult>> {
    private final List<UUID> bookIds;
}
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class GetBooksHandler implements RequestHandler<GetBooks, List<BookResult>> {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public List<BookResult> handle(GetBooks query) {
        return bookRepository.findAllByIdOrThrow(query.getBookIds())
                .stream()
                .map(bookMapper::toResult)
                .toList();
    }
}