package com.mercadolibro.exceptions;

import com.mercadolibro.Exception.ResourceAlreadyExistsException;
import com.mercadolibro.Exception.ResourceNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptions {
    @ExceptionHandler(NoBooksToShowException.class)
    public ResponseEntity<List<?>> processErrorNoBooksToShow() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
    }
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> processErrorBookNotFound(BookNotFoundException bookNotFoundEx){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: " + bookNotFoundEx.getMessage());
    }
    @ExceptionHandler(BookAlreadyExistsException.class)
    public ResponseEntity<String> processBookAlreadyExistsException(BookAlreadyExistsException bookAlreadyExistsException){
        return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: " + bookAlreadyExistsException.getMessage());
    }
    /*
    @ExceptionHandler(MethodArgumentNotValidException.class) @ResponseBody
    public ResponseEntity<List> processUnmergeException(final MethodArgumentNotValidException ex) {
        List list = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
    }
     */
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
