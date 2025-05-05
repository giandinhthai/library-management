package com.example.BorrowBookService.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan("com.example.BorrowBookService")  // Adjust the base package as needed
public class TestConfig {
}