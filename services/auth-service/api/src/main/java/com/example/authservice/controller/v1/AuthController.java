package com.example.authservice.controller.v1;

import com.example.authservice.dto.AuthTokenResult;
import com.example.authservice.usecase.command.Login;
import com.example.authservice.usecase.command.GetAccessToken;
import com.example.authservice.usecase.command.Register;
import com.example.authservice.usecase.query.ValidateToken;
import com.example.buildingblocks.cqrs.mediator.Mediator;
import com.example.buildingblocks.shared.api.DTO.RestApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final Mediator mediator;


    @PostMapping("/login")
    public ResponseEntity<RestApiResponse<AuthTokenResult>> login(@RequestBody Login command) {
        AuthTokenResult result = mediator.send(command);
        return ResponseEntity.ok(RestApiResponse.success(result, "Login successful"));

    }

    @PostMapping("/register")
    public ResponseEntity<RestApiResponse<Void>> register(@Valid @RequestBody Register command) {

        mediator.send(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestApiResponse.success(null, "User registered successfully"));

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RestApiResponse<AuthTokenResult>> refreshToken(@Valid @RequestBody GetAccessToken command) {

        AuthTokenResult result = mediator.send(command);
        return ResponseEntity.ok(RestApiResponse.success(result, "Token refreshed successfully"));


    }

    @PostMapping("/validate-token")
    public ResponseEntity<RestApiResponse<Boolean>> validateToken(@RequestBody ValidateToken command) {

        Boolean isValid = mediator.send(command);
        return ResponseEntity.ok(RestApiResponse.success(isValid, "Token validation completed"));

    }
}
