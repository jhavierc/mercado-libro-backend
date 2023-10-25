package com.mercadolibro.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, List<Map<String, String>>>> handleValidationException(ConstraintViolationException e) {
        Map<String, List<Map<String, String>>> errorResponse = new HashMap<>();
        List<Map<String, String>> validationErrors = new ArrayList<>();

        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            Map<String, String> error = new HashMap<>();
            error.put("field", violation.getPropertyPath().toString());
            error.put("message", violation.getMessage());
            validationErrors.add(error);
        }

        errorResponse.put("validation-error", validationErrors);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<String> handleResourceAlreadyExistsException(ResourceAlreadyExistsException e) {
        return ResponseEntity.status(409).body(e.getMessage());
    }

}
