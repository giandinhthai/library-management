package com.example.BorrowBookService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.example.BorrowBookService",
        "com.example.buildingblocks"
}
)
public class BorrowBookServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BorrowBookServiceApplication.class, args);
    }
}