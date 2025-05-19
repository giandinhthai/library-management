package com.example.BorrowBookService.eventHandler.domain_events;

import com.example.BorrowBookService.event.BookAvailableQuantityIncreasedEvent;
import com.example.BorrowBookService.usecase.command.member.UpdateNextPendingReservationOnBook;
import com.example.buildingblocks.cqrs.mediator.Mediator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookAvailableQuantityIncreasedEventHandler {
    private final Mediator mediator;
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void updateNextPendingReservationStatusOnBookReturnedEvent(BookAvailableQuantityIncreasedEvent bookEvent) {
        mediator.send(new UpdateNextPendingReservationOnBook(bookEvent.getBookId(), bookEvent.getQuantity()));
    }
}
