package com.example.BorrowBookService.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;
@AllArgsConstructor
@Getter
public class CustomAuthentication {
    private UUID userId;

}
