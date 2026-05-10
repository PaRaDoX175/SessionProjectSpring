package org.example.newsessionproject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class AbsalyamovRuslanExceptionHandler {
    @ExceptionHandler(AbsalyamovRuslanNotFoundException.class)
    public ResponseEntity<?> handleNotFoundEx(AbsalyamovRuslanNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("ex", ex.getMessage()));
    }

    @ExceptionHandler(AbsalyamovRuslanUserAlreadyExistException.class)
    public ResponseEntity<?> handleUserAlreadyExistEx(AbsalyamovRuslanUserAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("ex", ex.getMessage()));
    }
}
