package com.example.authservice.usecase.command;

import com.example.authservice.aggregate.AuthUser;
import com.example.authservice.dto.AuthTokenResult;
import com.example.authservice.exceptions.InvalidCredentialsException;
import com.example.authservice.repositories.AuthUserRepository;
import com.example.authservice.service.JwtAccessTokenGenerator;
import com.example.authservice.service.TokenGenerator;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Command;
import com.example.buildingblocks.security.encoder.PasswordEncoder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Getter
@NoArgsConstructor
public class Login implements Command<AuthTokenResult> {
    private String email;
    private String password;
}

@Service
@RequiredArgsConstructor
class LoginHandler implements RequestHandler<Login, AuthTokenResult> {
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;
    private final JwtAccessTokenGenerator jwtAccessTokenGenerator;

    @Override
    public AuthTokenResult handle(Login command) {
        AuthUser user = authUserRepository.findByEmail(command.getEmail())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!user.checkPassword(command.getPassword(), passwordEncoder)) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        var refreshToken = user.generateRefreshToken(tokenGenerator);
        var accessToken = jwtAccessTokenGenerator.generate(user);

        authUserRepository.save(user);

        return new AuthTokenResult(accessToken, refreshToken.getToken());
    }
}
