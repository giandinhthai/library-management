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
public class GenDataRunner implements CommandLineRunner {

    private final RandomBookJob randomBookJob;
    @Override
    public void run(String... args) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                "batch-user", // Principal (e.g., username)
                null, // Credentials (can be null for batch jobs)
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")) // Roles
        );

        // Set the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        randomBookJob.executeJob();
    }
}