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

    private final MemberRepository memberRepository;
    // should call command instead of handle here
    private final Mediator mediator;

    @Scheduled(cron = "0 0 0 * * *") // Run at midnight every day
    public void expireReservations() {
        mediator.send(new BatchExpireReservations());
    }
}
