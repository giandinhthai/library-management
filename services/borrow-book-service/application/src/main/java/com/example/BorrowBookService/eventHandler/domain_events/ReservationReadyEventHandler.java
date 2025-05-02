package com.example.BorrowBookService.eventHandler.domain_events;

import com.example.BorrowBookService.aggregate.Book;
import com.example.BorrowBookService.event.ReservationReadyEvent;
import com.example.BorrowBookService.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationReadyEventHandler {

    private final BookRepository bookRepository;

    @Async
    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void notifyMemberOnReservationReadyEvent(ReservationReadyEvent reservationReadyEvent) {
        log.info("notify member that book with id " + reservationReadyEvent.getReservation().getBookId() + " is ready");
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void updateBookStatusOnReservationReadyEvent(ReservationReadyEvent reservationReadyEvent) {
        Book book = bookRepository.findByIdOrThrow(reservationReadyEvent.getReservation().getBookId());
        book.approveReserved();
        bookRepository.save(book);
    }
}
