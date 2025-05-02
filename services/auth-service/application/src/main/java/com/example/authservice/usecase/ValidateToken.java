package com.example.authservice.usecase;

import com.example.authservice.service.JwtAccessTokenGenerator;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

public class ValidateToken {

    public record Command(
            @NotBlank(message = "Token is required")
            String token)
            implements com.example.buildingblocks.cqrs.request.Command<Boolean> {}

    @Component
    @AllArgsConstructor
    public static class Handler implements RequestHandler<Command, Boolean> {

        private final JwtAccessTokenGenerator jwtAccessTokenGenerator;


        @Override
        public Boolean handle(Command command) {
            return jwtAccessTokenGenerator.validateToken(command.token());
        }
    }
}