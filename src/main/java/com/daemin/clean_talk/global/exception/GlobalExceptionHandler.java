package com.daemin.clean_talk.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(
            IllegalStateException e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception e,
            HttpServletRequest request
    ) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                message,
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(errorResponse);
    }
}
