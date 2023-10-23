package com.mercadolibro.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)

    public ResponseEntity<String> handleValidationException(ConstraintViolationException e) {
        StringBuilder errorMessage = new StringBuilder("validation-error: [\n");


        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            errorMessage.append("  {\n");
            errorMessage.append("    field: \"").append(violation.getPropertyPath()).append("\",\n");
            errorMessage.append("    message: \"").append(violation.getMessage()).append("\"\n");
            errorMessage.append("  },\n");
        }

        errorMessage.append("]");

        return ResponseEntity.badRequest().body(errorMessage.toString());
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
