package com.example.BorrowBookService.eventHandler.domain_events;

import com.example.BorrowBookService.event.ReservationReadyEvent;
import com.example.BorrowBookService.usecase.command.book.ApproveBookReservation;
import com.example.buildingblocks.cqrs.mediator.Mediator;
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

    private final Mediator mediator;

    @Async
    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void notifyMemberOnReservationReadyEvent(ReservationReadyEvent reservationReadyEvent) {
        log.info("notify member that book with id {} is ready", reservationReadyEvent.getReservation().getBookId());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void updateBookStatusOnReservationReadyEvent(ReservationReadyEvent event) {
        mediator.send(new ApproveBookReservation(event.getReservation().getBookId()));
    }
}
