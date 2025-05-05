package com.example.BorrowBookService.handler;


import com.example.BorrowBookService.exception.NotFoundException;
import com.example.BorrowBookService.exception.UnexpectedBorrowStateException;
import com.example.BorrowBookService.exception.InvalidBookStateException;
import com.example.BorrowBookService.exception.InvalidBorrowRequestException;
import com.example.buildingblocks.shared.api.DTO.RestApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
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

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<RestApiResponse<Void>> handleEmailAlreadyExistsException(NotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(RestApiResponse.error("Not found error",
                        new RestApiResponse.ErrorDetails("INVALID_INPUT", ex.getMessage())));
    }

    @ExceptionHandler(UnexpectedBorrowStateException.class)
    public ResponseEntity<RestApiResponse<Void>> handleUnexpectedBorrowStateException(UnexpectedBorrowStateException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(RestApiResponse.error("Unexpected borrow state error",
                        new RestApiResponse.ErrorDetails("INVALID_INPUT", ex.getMessage())));
    }
    @ExceptionHandler(InvalidBookStateException.class)
    public ResponseEntity<RestApiResponse<Void>> handleUnvalidBookStateException(InvalidBookStateException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(RestApiResponse.error("Unvalid book state error",
                        new RestApiResponse.ErrorDetails("INVALID_INPUT", ex.getMessage())));
    }

    @ExceptionHandler(InvalidBorrowRequestException.class)
    public ResponseEntity<RestApiResponse<Void>> handleUnvalidBorrowRequestException(InvalidBorrowRequestException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(RestApiResponse.error("Unvalid borrow request error",
                        new RestApiResponse.ErrorDetails("INVALID_INPUT", ex.getMessage())));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RestApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(RestApiResponse.error("An unexpected error occurred",
                        new RestApiResponse.ErrorDetails("server error", ex.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestApiResponse<Void>> handleGeneralException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(RestApiResponse.error("An unexpected error occurred",
                        new RestApiResponse.ErrorDetails("SERVER_ERROR", ex.getMessage())));
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<RestApiResponse<Void>> handleBadCredentialsException(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(RestApiResponse.error("Bad credentials",
                        new RestApiResponse.ErrorDetails("BAD_CREDENTIALS", ex.getMessage())));
    }
}
