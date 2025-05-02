package com.example.BorrowBookService.eventHandler;

import com.example.BorrowBookService.aggregate.Book;
import com.example.BorrowBookService.aggregate.Member;
import com.example.BorrowBookService.event.BooksBorrowedEvent;
import com.example.BorrowBookService.event.FineAmountUpdatedEvent;
import com.example.BorrowBookService.repository.BookRepository;
import com.example.BorrowBookService.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class BookBorrowedEventHandler {
    private final BookRepository bookRepository;
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @EventListener
    public void onBooksBorrowedEvent(BooksBorrowedEvent booksBorrowedEvent) {
        System.out.println("Book borrowed event received");
        List<Book> books = bookRepository.findAllByIdOrThrow(booksBorrowedEvent.getBookIds());

        for (Book book : books) {
            book.getBorrowed();
            bookRepository.save(book);
        }



    }
}
