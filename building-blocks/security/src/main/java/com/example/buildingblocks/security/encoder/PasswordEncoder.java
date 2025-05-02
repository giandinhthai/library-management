package com.example.buildingblocks.security.encoder;

public interface PasswordEncoder {
    boolean matches(String raw, String hashed);
    String encode(String raw);
}