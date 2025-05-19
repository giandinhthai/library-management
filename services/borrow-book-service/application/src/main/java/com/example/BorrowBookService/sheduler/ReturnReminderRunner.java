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
public class ReturnReminderRunner implements CommandLineRunner {

    private final ReturnReminderScheduler returnReminderScheduler;
    @Override
    @Transactional
    public void run(String... args) {

        returnReminderScheduler.sendScheduledReminders();
    }
}