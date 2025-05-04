package com.example.BorrowBookService.usecase.command;

import com.example.BorrowBookService.aggregate.Book;
import com.example.BorrowBookService.repository.BookRepository;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBooksStatusOnBorrow implements Command<List<UUID>> {
    private List<UUID> bookIds;
}
@Service
@AllArgsConstructor
@Slf4j
class UpdateBooksStatusOnBorrowHandler implements RequestHandler<UpdateBooksStatusOnBorrow, List<UUID>> {
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public List<UUID> handle(UpdateBooksStatusOnBorrow command) {
        List<Book> books = bookRepository.findAllByIdOrThrow(command.getBookIds());
        for (Book book : books) {
            book.getBorrowed();
        }
        bookRepository.saveAll(books);
        return books.stream()
                .map(Book::getBookId)
                .toList();
    }
}