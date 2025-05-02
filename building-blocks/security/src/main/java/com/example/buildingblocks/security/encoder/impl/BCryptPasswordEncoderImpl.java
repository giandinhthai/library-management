package com.example.buildingblocks.security.encoder.impl;

import com.example.buildingblocks.security.encoder.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordEncoderImpl implements PasswordEncoder {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public boolean matches(String raw, String hashed) {
        return encoder.matches(raw, hashed);
    }

    @Override
    public String encode(String raw) {
        return encoder.encode(raw);
    }
}