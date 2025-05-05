package com.example.BorrowBookService.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class JwtDecoder {
    private final ObjectMapper mapper;

    public JwtDecoder(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JwtClaims decodeWithoutVerify(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) throw new RuntimeException("Invalid token");

        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(parts[1]), StandardCharsets.UTF_8);

        try {
            return mapper.readValue(payload, JwtClaims.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to decode payload", e);
        }
    }
}
