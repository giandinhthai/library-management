package com.example.BorrowBookService.sheduler;

import com.example.BorrowBookService.usecase.command.batch.BatchExpireReservations;
import com.example.buildingblocks.cqrs.mediator.Mediator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationExpirationScheduler {

    private final Mediator mediator;

    @Scheduled(cron = "0 0 0 * * *") // Run at midnight every day
    public void expireReservations() {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                "batch-user", // Principal (e.g., username)
                null, // Credentials (can be null for batch jobs)
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")) // Roles
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("Starting reservation expiration check");
        var totalExpired = mediator.send(new BatchExpireReservations());
        log.info("Completed reservation expiration check. Expired {} reservations", totalExpired);
    }
}
