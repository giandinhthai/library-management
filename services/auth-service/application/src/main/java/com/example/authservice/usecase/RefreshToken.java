package com.example.authservice.usecase;

import com.example.authservice.aggregate.AuthUser;
import com.example.authservice.dto.AuthTokenResult;
import com.example.authservice.exceptions.InvalidTokenException;
import com.example.authservice.repositories.AuthUserRepository;
import com.example.authservice.service.JwtAccessTokenGenerator;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

public class RefreshToken {

    public record Command(
            @NotBlank(message = "Refresh token is required")
            String refreshToken)
            implements com.example.buildingblocks.cqrs.request.Command<AuthTokenResult> {
    }

    @Component
    @AllArgsConstructor
    public static class Handler implements RequestHandler<Command, AuthTokenResult> {

        private AuthUserRepository authUserRepository;

        private JwtAccessTokenGenerator jwtAccessTokenGenerator;

        @Override
        public AuthTokenResult handle(Command command) {
            // Find user with the given refresh token
            AuthUser user = findUserByRefreshToken(command.refreshToken());

            // Validate the refresh token
            user.validateRefreshToken(command.refreshToken());

            // Generate new access token
            String accessToken = jwtAccessTokenGenerator.generate(user);

            // Return the tokens
            return new AuthTokenResult(accessToken, command.refreshToken());
        }

        private AuthUser findUserByRefreshToken(String refreshToken) {
            return authUserRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new NoSuchElementException("Invalid refresh token"));
        }


    }
}