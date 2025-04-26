package com.example.BorrowBookService.client;

import com.example.BorrowBookService.client.itf.AuthServiceClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@Slf4j
public class JWTServiceClient implements AuthServiceClient {

    @Data
    private static class TokenValidationResponse {
        private boolean success;
        private String message;
        private boolean data;
        private String timestamp;
    }

    private final RestTemplate restTemplate;

    public JWTServiceClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Override
    public boolean validateToken(String token) {
        String url = "http://localhost:8081/api/v1/auth/validate-token";
        Map<String, String> body = Map.of("token", token);

        try {
            ResponseEntity<TokenValidationResponse> response = restTemplate.postForEntity(url, body, TokenValidationResponse.class);
            TokenValidationResponse validationResponse = response.getBody();
            return validationResponse != null && validationResponse.isSuccess() && validationResponse.isData();
        } catch (HttpClientErrorException.Unauthorized | HttpClientErrorException.Forbidden e) {
            return false;
        } catch (Exception e) {
            log.error("Error validating token with auth service", e);
            throw new RuntimeException("Failed to validate token with auth service", e);
        }
    }
}
