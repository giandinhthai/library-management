//package com.example.BorrowBookService.client;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//@Component
//public class AuthServiceClientImpl implements AuthServiceClient {
//    private final RestTemplate restTemplate;
//
//    @Value("${auth-service.url}")
//    private final String authServiceUrl;
//
//
//
//    @Override
//    public boolean validateToken(String token) {
//        String url = authServiceUrl + "/api/v1/auth/validate-token";
//        ResponseEntity<ApiResponse<Boolean>> response = restTemplate.postForEntity(
//                url,
//                new ValidateTokenRequest(token),
//                new ParameterizedTypeReference<ApiResponse<Boolean>>() {}
//        );
//        return response.getBody().getData();
//    }
//}
