package com.example.BorrowBookService.sheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class StartupReservationExpirationRunner implements CommandLineRunner {

    private final ReservationExpirationScheduler expirationScheduler;
    @Override
    @Transactional
    public void run(String... args) {
        log.info("Running initial reservation expiration check on application startup");
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                "batch-user", // Principal (e.g., username)
                null, // Credentials (can be null for batch jobs)
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")) // Roles
        );

        // Set the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        expirationScheduler.expireReservations();
        SecurityContextHolder.clearContext();
    }
}