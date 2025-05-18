package com.example.BorrowBookService.usecase.command.book;

import com.example.BorrowBookService.aggregate.Book;
import com.example.BorrowBookService.repository.BookRepository;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Command;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBooksStatusOnReturn implements Command<List<UUID>> {
    private List<UUID> bookIds;
}
@Service
@RequiredArgsConstructor
@Slf4j
class UpdateBooksStatusOnReturnHandler implements RequestHandler<UpdateBooksStatusOnReturn, List<UUID>> {
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public List<UUID> handle(UpdateBooksStatusOnReturn command) {
        List<Book> books = bookRepository.findAllByIdForUpdateOrThrow(command.getBookIds());

        for (Book book : books) {
            book.isReturned();
        }

        bookRepository.saveAll(books);
        return books.stream()
                .map(Book::getBookId)
                .toList();
    }
}