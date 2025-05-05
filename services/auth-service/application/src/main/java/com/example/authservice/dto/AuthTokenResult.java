package com.example.authservice.dto;


import lombok.Getter;

public record AuthTokenResult(String accessToken, String refreshToken) {

}