package com.example.BorrowBookService.sheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class StartupReservationExpirationRunner implements CommandLineRunner {

    private final ReservationExpirationScheduler expirationScheduler;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Running initial reservation expiration check on application startup");
        expirationScheduler.expireReservations();
    }
}