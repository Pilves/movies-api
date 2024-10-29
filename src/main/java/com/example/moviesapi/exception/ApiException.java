package com.example.moviesapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
    private final ErrorCode code;
    private final HttpStatus status;

    public ApiException(ErrorCode code, String message) {
        super(message);
        this.code = code;
        this.status = code.getStatus();
    }

    public ApiException(ErrorCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.status = code.getStatus();
    }

    public static ApiException notFound(String message) {
        return new ApiException(ErrorCode.RESOURCE_NOT_FOUND, message);
    }

    public static ApiException duplicate(String message) {
        return new ApiException(ErrorCode.DUPLICATE_RESOURCE, message);
    }

    public static ApiException invalidRequest(String message) {
        return new ApiException(ErrorCode.INVALID_REQUEST, message);
    }

    public static ApiException constraintViolation(String message) {
        return new ApiException(ErrorCode.CONSTRAINT_VIOLATION, message);
    }

    public static ApiException operationFailed(String message) {
        return new ApiException(ErrorCode.OPERATION_FAILED, message);
    }
}
