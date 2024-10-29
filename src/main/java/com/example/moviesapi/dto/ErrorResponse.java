package com.example.moviesapi.dto;

import com.example.moviesapi.exception.ErrorCode;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String code;
    private String message;
    private String path;

    public static ErrorResponse of(ErrorCode code, String message, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(code.getStatus().value())
                .code(code.name())
                .message(message)
                .path(path)
                .build();
    }
}