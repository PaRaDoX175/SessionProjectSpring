package org.example.newsessionproject.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class AbsalyamovRuslanExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(AbsalyamovRuslanExceptionHandler.class);

    @ExceptionHandler(AbsalyamovRuslanNotFoundException.class)
    public ResponseEntity<?> handleNotFoundEx(AbsalyamovRuslanNotFoundException ex, HttpServletRequest request) {
        log.warn("Not found error on path={} message={}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("ex", ex.getMessage()));
    }

    @ExceptionHandler(AbsalyamovRuslanUserAlreadyExistException.class)
    public ResponseEntity<?> handleUserAlreadyExistEx(AbsalyamovRuslanUserAlreadyExistException ex, HttpServletRequest request) {
        log.warn("User already exists on path={} message={}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("ex", ex.getMessage()));
    }

    @ExceptionHandler(AbsalyamovRuslanAccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedEx(AbsalyamovRuslanAccessDeniedException ex, HttpServletRequest request) {
        log.warn("Access denied on path={} message={}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("ex", ex.getMessage()));
    }
}
