package com.example.authservice.usecase.command;

import com.example.authservice.aggregate.AuthUser;
import com.example.authservice.aggregate.Role;
import com.example.authservice.exceptions.EmailAlreadyExistsException;
import com.example.authservice.repositories.AuthUserRepository;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Command;
import com.example.buildingblocks.security.encoder.PasswordEncoder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class Register implements Command<Void> {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    @NotNull(message = "Role is required")
    private Role role;
}

@Service
@RequiredArgsConstructor
class RegisterHandler implements RequestHandler<Register, Void> {
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Void handle(Register command) {
        if (!command.getPassword().equals(command.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (authUserRepository.findByEmail(command.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        var user = AuthUser.create(
                command.getEmail(),
                command.getPassword(),
                command.getRole(),
                passwordEncoder
        );

        authUserRepository.save(user);
        return null;
    }
}
