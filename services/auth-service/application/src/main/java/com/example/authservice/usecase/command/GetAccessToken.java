package com.example.authservice.usecase.command;

import com.example.authservice.aggregate.AuthUser;
import com.example.authservice.aggregate.RefreshToken;
import com.example.authservice.dto.AuthTokenResult;
import com.example.authservice.repositories.AuthUserRepository;
import com.example.authservice.service.JwtAccessTokenGenerator;
import com.example.authservice.service.TokenGenerator;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Command;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Getter
@NoArgsConstructor

public class GetAccessToken implements Command<AuthTokenResult> {
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}

@Service
@RequiredArgsConstructor
class GetAccessTokenHandler implements RequestHandler<GetAccessToken, AuthTokenResult> {
    private final AuthUserRepository authUserRepository;
    private final JwtAccessTokenGenerator jwtAccessTokenGenerator;
    private final TokenGenerator tokenGenerator;
    @Override
    public AuthTokenResult handle(GetAccessToken command) {
        // Find user with the given refresh token
        AuthUser user = findUserByRefreshToken(command.getRefreshToken());

        // Validate the refresh token
        user.validateAndRevokeRefreshToken(command.getRefreshToken());
        var refreshToken = user.generateRefreshToken(tokenGenerator);
        // Generate new access token
        String accessToken = jwtAccessTokenGenerator.generate(user);

        // Return the tokens
        return new AuthTokenResult(accessToken, refreshToken.getToken());
    }

    private AuthUser findUserByRefreshToken(String refreshToken) {
        return authUserRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NoSuchElementException("Invalid refresh token"));
    }
}
