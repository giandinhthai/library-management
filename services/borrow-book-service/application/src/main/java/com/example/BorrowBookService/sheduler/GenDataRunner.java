package com.example.BorrowBookService.sheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenDataRunner implements CommandLineRunner {

    private final RandomBookJob randomBookJob;

    @Override
    public void run(String... args) {

        randomBookJob.executeJob();
    }
}