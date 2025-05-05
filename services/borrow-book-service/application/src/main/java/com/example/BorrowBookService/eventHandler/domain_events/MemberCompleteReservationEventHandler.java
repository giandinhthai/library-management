package com.example.BorrowBookService.eventHandler.domain_events;

import com.example.BorrowBookService.event.MemberCompleteReservationEvent;
import com.example.BorrowBookService.usecase.command.CompleteBookReservation;
import com.example.buildingblocks.cqrs.mediator.Mediator;
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
    private final Mediator mediator;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void updateBookStatusOnReservationCompleted(MemberCompleteReservationEvent event) {
        log.info("Processing reservation completion for book: {}", event.getReservation().getBookId());
        mediator.send(new CompleteBookReservation(event.getReservation().getBookId()));
        log.info("Successfully processed reservation completion for book: {}", event.getReservation().getBookId());
    }
}
