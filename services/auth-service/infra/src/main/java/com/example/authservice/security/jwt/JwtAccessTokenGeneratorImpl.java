package com.example.authservice.security.jwt;

import com.example.authservice.aggregate.AuthUser;
import com.example.authservice.service.JwtAccessTokenGenerator;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtAccessTokenGeneratorImpl implements JwtAccessTokenGenerator {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.token-validity-in-seconds}")
    private long tokenValidityInSeconds;

    @Override
    public String generate(AuthUser user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInSeconds * 1000);
        String authorities = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(user.getUserId().toString())
                .claim("auth", authorities)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        return io.jsonwebtoken.security.Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}