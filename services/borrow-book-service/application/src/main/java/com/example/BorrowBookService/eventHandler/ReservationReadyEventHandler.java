package com.example.BorrowBookService.eventHandler;

import com.example.BorrowBookService.event.ReservationReadyEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ReservationReadyEventHandler {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void notifyMemberOnReservationReadyEvent(ReservationReadyEvent reservationReadyEvent) {
        System.out.println("Reservation ready event received");
        System.out.println("Notifying member " + reservationReadyEvent.getMemberId() + " that book " + reservationReadyEvent.getBookId() + " is ready");
    }
}
