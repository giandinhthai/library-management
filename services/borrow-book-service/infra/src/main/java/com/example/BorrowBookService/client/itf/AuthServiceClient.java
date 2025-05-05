package com.example.BorrowBookService.client.itf;

public interface AuthServiceClient {
    boolean validateToken(String token);
}
