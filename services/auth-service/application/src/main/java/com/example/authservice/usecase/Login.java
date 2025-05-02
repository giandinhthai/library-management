package com.example.authservice.usecase;

import com.example.authservice.aggregate.AuthUser;
import com.example.authservice.dto.AuthTokenResult;
import com.example.authservice.exceptions.InvalidCredentialsException;
import com.example.authservice.repositories.AuthUserRepository;
import com.example.authservice.service.JwtAccessTokenGenerator;
import com.example.authservice.service.TokenGenerator;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.security.encoder.PasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

public class Login {

    public record Command(String email, String password) implements com.example.buildingblocks.cqrs.request.Command<AuthTokenResult> {}

    @Component
    @AllArgsConstructor
    public static class Handler implements RequestHandler<Command, AuthTokenResult> {

        private AuthUserRepository authUserRepository;

        private PasswordEncoder passwordEncoder;

        private TokenGenerator tokenGenerator;

        private JwtAccessTokenGenerator jwtAccessTokenGenerator;

        @Override
        public AuthTokenResult handle(Command command) {
            AuthUser user = authUserRepository.findByEmail(command.email())
                    .orElseThrow(() -> new NoSuchElementException("User not found"));

            if (!user.checkPassword(command.password(), passwordEncoder)) {
                throw new InvalidCredentialsException("Invalid credentials");
            }

            var refreshToken = user.generateRefreshToken(tokenGenerator);
            var accessToken = jwtAccessTokenGenerator.generate(user);

            authUserRepository.save(user);

            return new AuthTokenResult(accessToken, refreshToken.getToken());
        }
    }
}