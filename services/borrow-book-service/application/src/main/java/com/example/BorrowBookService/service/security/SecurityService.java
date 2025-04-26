package com.example.BorrowBookService.service.security;

public interface SecurityService {
    String getCurrentUserId();
    boolean hasRole(String role);
}
