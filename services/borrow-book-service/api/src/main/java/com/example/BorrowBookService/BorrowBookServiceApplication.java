package com.example.BorrowBookService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {
        "com.example.BorrowBookService",
        "com.example.buildingblocks"
})
@EnableAsync
@EnableRetry
public class BorrowBookServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BorrowBookServiceApplication.class, args);
    }
}