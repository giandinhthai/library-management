package com.example.BorrowBookService.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.example.BorrowBookService.jpa.mapper"})
public class MapperConfig {
}