package com.example.moviesapi.exception;

import com.example.moviesapi.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.of(
                ex.getCode(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(ex.getStatus()).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        String message = "Data integrity violation";
        if (ex.getCause() instanceof ConstraintViolationException) {
            String constraintName = ((ConstraintViolationException) ex.getCause()).getConstraintName();

            if (constraintName != null) {
                message = switch (constraintName.toLowerCase()) {
                    case "uk_actor_name" -> "An actor with this name already exists";
                    case "uk_genre_name" -> "A genre with this name already exists";
                    default -> "A database constraint was violated";
                };
            }
        }

        return ResponseEntity
                .status(ErrorCode.CONSTRAINT_VIOLATION.getStatus())
                .body(ErrorResponse.of(
                        ErrorCode.CONSTRAINT_VIOLATION,
                        message,
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validation failed");

        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getStatus())
                .body(ErrorResponse.of(
                        ErrorCode.INVALID_REQUEST,
                        message,
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponse> handleDateTimeParseException(
            DateTimeParseException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getStatus())
                .body(ErrorResponse.of(
                        ErrorCode.INVALID_REQUEST,
                        "Invalid date format: " + ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllOtherExceptions(
            Exception ex,
            HttpServletRequest request) {

        logger.error("Unexpected error occurred", ex);

        return ResponseEntity
                .status(ErrorCode.OPERATION_FAILED.getStatus())
                .body(ErrorResponse.of(
                        ErrorCode.OPERATION_FAILED,
                        "An unexpected error occurred",
                        request.getRequestURI()
                ));
    }
}