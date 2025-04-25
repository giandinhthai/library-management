package com.example.authservice.usecase.query;

import com.example.authservice.service.JwtAccessTokenGenerator;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Query;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Getter
@NoArgsConstructor
public class ValidateToken implements Query<Boolean> {
    @NotBlank(message = "Token is required")
    private String token;
}

@Service
@RequiredArgsConstructor
class ValidateTokenHandler implements RequestHandler<ValidateToken, Boolean> {
    private final JwtAccessTokenGenerator jwtAccessTokenGenerator;

    @Override
    public Boolean handle(ValidateToken query) {
        return jwtAccessTokenGenerator.validateToken(query.getToken());
    }
}
