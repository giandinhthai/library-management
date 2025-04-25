package com.example.BorrowBookService.eventHandler;

import com.example.BorrowBookService.aggregate.Book;
import com.example.BorrowBookService.event.MemberBorrowBooksEvent;
import com.example.BorrowBookService.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class MemberBorrowBooksEventHandler {
    private final BookRepository bookRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void updateBookStatusOnMemberBorrowedEvent(MemberBorrowBooksEvent memberBorrowBooksEvent) {
        List<UUID> bookIds = memberBorrowBooksEvent.getBookIds().stream().toList();
        List<Book> books = bookRepository.findAllByIdOrThrow(bookIds);
        for (Book book : books) {
            book.getBorrowed();
        }
        bookRepository.saveAll(books);
    }
}
