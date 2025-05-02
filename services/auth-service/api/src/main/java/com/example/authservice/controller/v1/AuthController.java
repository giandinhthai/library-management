package com.example.authservice.controller.v1;

import com.example.authservice.dto.AuthTokenResult;
import com.example.authservice.usecase.Login;
import com.example.authservice.usecase.RefreshToken;
import com.example.authservice.usecase.Register;
import com.example.authservice.usecase.ValidateToken;
import com.example.buildingblocks.cqrs.mediator.Mediator;
import com.example.buildingblocks.shared.api.DTO.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final Mediator mediator;

    public AuthController(Mediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthTokenResult>> login(@RequestBody Login.Command command) {
        AuthTokenResult result = mediator.send(command);
        return ResponseEntity.ok(ApiResponse.success(result, "Login successful"));

    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody Register.Command command) {

        mediator.send(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null, "User registered successfully"));

    }
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthTokenResult>> refreshToken(@Valid @RequestBody RefreshToken.Command command) {

        AuthTokenResult result = mediator.send(command);
        return ResponseEntity.ok(ApiResponse.success(result, "Token refreshed successfully"));


    }
    @PostMapping("/validate-token")
    public ResponseEntity<ApiResponse<Boolean>> validateToken(@RequestBody ValidateToken.Command command) {

        Boolean isValid = mediator.send(command);
        return ResponseEntity.ok(ApiResponse.success(isValid, "Token validation completed"));

    }
}
