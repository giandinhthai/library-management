package com.example.BorrowBookService.handler;


import com.example.BorrowBookService.exception.NotFoundException;
import com.example.BorrowBookService.exception.UnexpectedBorrowStateException;
import com.example.BorrowBookService.exception.InvalidBookStateException;
import com.example.BorrowBookService.exception.InvalidBorrowRequestException;
import com.example.buildingblocks.shared.api.DTO.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        HashMap<Object, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Validation failed",
                        new ApiResponse.ErrorDetails("VALIDATION_ERROR", errors.toString())));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailAlreadyExistsException(NotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error("Not found error",
                        new ApiResponse.ErrorDetails("INVALID_INPUT", ex.getMessage())));
    }

    @ExceptionHandler(UnexpectedBorrowStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpectedBorrowStateException(UnexpectedBorrowStateException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error("Unexpected borrow state error",
                        new ApiResponse.ErrorDetails("INVALID_INPUT", ex.getMessage())));
    }
    @ExceptionHandler(InvalidBookStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnvalidBookStateException(InvalidBookStateException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error("Unvalid book state error",
                        new ApiResponse.ErrorDetails("INVALID_INPUT", ex.getMessage())));
    }

    @ExceptionHandler(InvalidBorrowRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnvalidBorrowRequestException(InvalidBorrowRequestException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error("Unvalid borrow request error",
                        new ApiResponse.ErrorDetails("INVALID_INPUT", ex.getMessage())));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("An unexpected error occurred",
                        new ApiResponse.ErrorDetails("server error", ex.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An unexpected error occurred",
                        new ApiResponse.ErrorDetails("SERVER_ERROR", ex.getMessage())));
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Bad credentials",
                        new ApiResponse.ErrorDetails("BAD_CREDENTIALS", ex.getMessage())));
    }
}
