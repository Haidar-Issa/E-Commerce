package com.example.test.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiResponse<T> {
    private HttpStatus status;
    private String message;
    private T data;
    private String path;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> create(HttpStatus status, String message, T data, String path) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }

}
