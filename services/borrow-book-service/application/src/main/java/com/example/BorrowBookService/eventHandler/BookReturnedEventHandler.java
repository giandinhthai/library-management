package com.example.BorrowBookService.eventHandler;

import com.example.BorrowBookService.aggregate.Book;
import com.example.BorrowBookService.event.BooksBorrowedEvent;
import com.example.BorrowBookService.event.BooksReturnedEvent;
import com.example.BorrowBookService.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@AllArgsConstructor
public class BookReturnedEventHandler {
    private final BookRepository bookRepository;
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @EventListener
    public void onBooksBorrowedEvent(BooksReturnedEvent booksReturnedEvent) {
        System.out.println("Book borrowed event received");
        List<Book> books = bookRepository.findAllByIdOrThrow(booksReturnedEvent.getBookIds());

        for (Book book : books) {
            book.isReturned();
        }
        bookRepository.saveAll(books);


    }
}
