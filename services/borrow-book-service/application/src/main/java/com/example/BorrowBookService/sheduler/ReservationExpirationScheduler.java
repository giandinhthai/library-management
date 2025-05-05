package com.example.BorrowBookService.sheduler;

import com.example.BorrowBookService.repository.MemberRepository;
import com.example.BorrowBookService.usecase.command.BatchExpireReservations;
import com.example.buildingblocks.cqrs.mediator.Mediator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationExpirationScheduler {

    private final Mediator mediator;

    @Scheduled(cron = "0 0 0 * * *") // Run at midnight every day
    public void expireReservations() {
        log.info("Starting reservation expiration check");
        var totalExpired = mediator.send(new BatchExpireReservations());
        log.info("Completed reservation expiration check. Expired {} reservations", totalExpired);
    }
}
