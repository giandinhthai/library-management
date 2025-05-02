package com.example.BorrowBookService.eventHandler.domain_events;

import com.example.BorrowBookService.aggregate.Reservation;
import com.example.BorrowBookService.event.ReservationExpiredEvent;
import com.example.BorrowBookService.repository.MemberRepository;
import com.example.BorrowBookService.repository.ReservationReadOnlyRepository;
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
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ReservationExpiredEventHandler {
    private final MemberRepository memberRepository;
    private final ReservationReadOnlyRepository reservationReadOnlyRepository;
    @Async
    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onReservationExpiredEvent(ReservationExpiredEvent reservationExpiredEvent) {
        var nextPendingReservation =
                reservationReadOnlyRepository.getNextReservationOnBook(reservationExpiredEvent.getBookId());
        if (nextPendingReservation == null) {
            return;
        }
        var member = nextPendingReservation.getMember();
        member.markAsReadyReservationContains(nextPendingReservation);
        memberRepository.save(member);
    }
}
