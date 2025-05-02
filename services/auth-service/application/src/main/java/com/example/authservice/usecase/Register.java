package com.example.authservice.usecase;

import com.example.authservice.aggregate.AuthUser;
import com.example.authservice.aggregate.Role;
import com.example.authservice.exceptions.EmailAlreadyExistsException;
import com.example.authservice.repositories.AuthUserRepository;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.security.encoder.PasswordEncoder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public class Register {

    public record Command(
            @NotBlank(message = "Email is required")
            @Email(message = "Email should be valid")
            String email,

            @NotBlank(message = "Password is required")
            @Size(min = 8, message = "Password must be at least 8 characters long")
            String password,

            @NotBlank(message = "Confirm password is required")
            String confirmPassword,
            @NotBlank(message = "Role is required")
            Role role)


            implements com.example.buildingblocks.cqrs.request.Command<Void> {
    }

    @Component
    @AllArgsConstructor
    public static class Handler implements RequestHandler<Command, Void> {

        private AuthUserRepository authUserRepository;

        private PasswordEncoder passwordEncoder;
        private static final Set<Role> ALLOWED_ROLES = EnumSet.of(Role.Member);

        @Override
        public Void handle(Command command) {


            // Validate email is not already in use
            if (authUserRepository.findByEmail(command.email()).isPresent()) {
                throw new EmailAlreadyExistsException("Email already in use");
            }

            // Validate password matches confirmation
            if (!command.password().equals(command.confirmPassword())) {
                throw new IllegalArgumentException("Passwords do not match");
            }
            if (!ALLOWED_ROLES.contains(command.role())) {
                throw new IllegalArgumentException("Invalid role. Allowed roles are: " +
                        Arrays.toString(ALLOWED_ROLES.toArray()));
            }
            // Hash the password
            String hashedPassword = passwordEncoder.encode(command.password());

            // Create new user with USER role

            AuthUser newUser = AuthUser.create(command.email(), hashedPassword);
            newUser.assignRole(command.role());

            // Save the user
            authUserRepository.save(newUser);

            return null;
        }
    }
}
