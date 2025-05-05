package com.example.BorrowBookService.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JwtClaims {
    private String sub;
    private String auth;
    private long iat;
    private long exp;
}
