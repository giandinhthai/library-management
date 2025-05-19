package com.example.BorrowBookService.sheduler;

import com.example.BorrowBookService.repository.BorrowReadOnlyRepository;
import com.example.BorrowBookService.usecase.command.BatchNotifyDueSoonBorrowers;
import com.example.buildingblocks.cqrs.mediator.Mediator;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class ReturnReminderScheduler {
    private final Mediator mediator;
    // Runs daily at 8 AM
    @Scheduled(cron = "0 0 0 * * *")
    public void sendScheduledReminders() {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                "batch-user", // Principal (e.g., username)
                null, // Credentials (can be null for batch jobs)
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")) // Roles
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        mediator.send(new BatchNotifyDueSoonBorrowers());
        SecurityContextHolder.clearContext();
    }


}