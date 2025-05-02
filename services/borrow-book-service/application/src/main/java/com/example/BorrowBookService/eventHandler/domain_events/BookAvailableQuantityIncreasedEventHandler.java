package com.example.BorrowBookService.eventHandler.domain_events;

import com.example.BorrowBookService.aggregate.Member;
import com.example.BorrowBookService.aggregate.Reservation;
import com.example.BorrowBookService.event.BookAvailableQuantityIncreasedEvent;
import com.example.BorrowBookService.repository.MemberRepository;
import com.example.BorrowBookService.repository.ReservationReadOnlyRepository;
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
    private final MemberRepository memberRepository;
    private final ReservationReadOnlyRepository reservationReadOnlyRepository;
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void updateNextPendingReservationStatusOnBookReturnedEvent(BookAvailableQuantityIncreasedEvent bookAvailableQuantityIncreasedEvent) {
        log.info("Member reservation status updated");
        var bookUUID = bookAvailableQuantityIncreasedEvent.getBookId();
        Reservation nextPendingReservation = reservationReadOnlyRepository.getNextReservationOnBook(bookUUID);
        if (nextPendingReservation == null) {
            log.info("no pending reservation for book {}", bookUUID);
            return;
        }
        Member member = nextPendingReservation.getMember();
        member.markAsReadyReservationContains(nextPendingReservation);
        memberRepository.save(member);
    }
}
