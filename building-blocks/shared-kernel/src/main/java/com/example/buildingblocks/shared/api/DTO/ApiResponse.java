package com.example.buildingblocks.shared.api.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private ErrorDetails error;

    private ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.data = data;
        response.message = "Operation completed successfully";
        return response;
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.data = data;
        response.message = message;
        return response;
    }

    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.message = message;
        return response;
    }

    public static <T> ApiResponse<T> error(String message, ErrorDetails errorDetails) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.message = message;
        response.error = errorDetails;
        return response;
    }

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public ErrorDetails getError() {
        return error;
    }

    // Error details class for more specific error information
    public static class ErrorDetails {
        private String code;
        private String detail;
        private String field;

        public ErrorDetails(String code, String detail) {
            this.code = code;
            this.detail = detail;
        }

        public ErrorDetails(String code, String detail, String field) {
            this.code = code;
            this.detail = detail;
            this.field = field;
        }

        // Getters
        public String getCode() {
            return code;
        }

        public String getDetail() {
            return detail;
        }

        public String getField() {
            return field;
        }
    }
}