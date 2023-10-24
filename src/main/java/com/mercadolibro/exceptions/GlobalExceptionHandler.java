package com.mercadolibro.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> processErrorResourceNotFound(ResourceNotFoundException resNotFoundEx){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: "+ resNotFoundEx.getMessage());
    }
}
