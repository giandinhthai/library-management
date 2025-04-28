package com.example.BorrowBookService.service.security;

import java.util.UUID;

public interface SecurityService {
    UUID getCurrentUserId();
    boolean hasRole(String role);
}
