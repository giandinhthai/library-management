package com.example.BorrowBookService.eventHandler;

import com.example.BorrowBookService.aggregate.Book;
import com.example.BorrowBookService.event.MemberCompleteReservationEvent;
import com.example.BorrowBookService.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Handles the event when a member completes a reservation by borrowing the book.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MemberCompleteReservationEventHandler {
    private final BookRepository bookRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void updateBookStatusOnReservationCompleted(MemberCompleteReservationEvent event) {
        log.info("Processing reservation completion for book: {}", event.getReservation().getBookId());
        
        Book book = bookRepository.findByIdOrThrow(event.getReservation().getBookId());
        book.completeReservation();
        bookRepository.save(book);
        
        log.info("Successfully processed reservation completion for book: {}", event.getReservation().getBookId());
    }
}
