package com.example.authservice.exceptions.handler;


import com.example.authservice.exceptions.EmailAlreadyExistsException;
import com.example.buildingblocks.shared.api.DTO.RestApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        HashMap<Object, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(RestApiResponse.error("Validation failed",
                        new RestApiResponse.ErrorDetails("VALIDATION_ERROR", errors.toString())));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<RestApiResponse<Void>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(RestApiResponse.error("Registration failed",
                        new RestApiResponse.ErrorDetails("EMAIL_EXISTS", ex.getMessage())));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RestApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(RestApiResponse.error("Invalid input",
                        new RestApiResponse.ErrorDetails("INVALID_INPUT", ex.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestApiResponse<Void>> handleGeneralException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(RestApiResponse.error("An unexpected error occurred",
                        new RestApiResponse.ErrorDetails("SERVER_ERROR", ex.getMessage())));
    }
}
