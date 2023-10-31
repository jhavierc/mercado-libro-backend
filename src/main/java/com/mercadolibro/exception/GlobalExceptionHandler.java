package com.mercadolibro.exception;

import com.mercadolibro.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoBooksToShowException.class)
    public ResponseEntity<List<?>> processErrorNoBooksToShow() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
    }
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> processErrorBookNotFound(BookNotFoundException bookNotFoundEx){
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(bookNotFoundEx.getMessage(), HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    @ExceptionHandler(BookAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> processBookAlreadyExistsException(BookAlreadyExistsException bookAlreadyExistsException){
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(bookAlreadyExistsException.getMessage(), HttpStatus.CONFLICT.value());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<Map<String, String>>>> processUnmergeException(final MethodArgumentNotValidException ex) {
        Map<String, List<Map<String, String>>> errorResponse = new HashMap<>();
        List<Map<String, String>> validationErrors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("field", ((FieldError) error).getField());
            errorMap.put("message", error.getDefaultMessage());
            validationErrors.add(errorMap);
        });

        errorResponse.put("validation-error", validationErrors);

        return ResponseEntity.badRequest().body(errorResponse);
    }
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
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.badRequest().body(errorResponse);
    }
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceAlreadyExistsException(ResourceAlreadyExistsException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage(), HttpStatus.CONFLICT.value());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    @ExceptionHandler(IncorrectDateFormatException.class)
    public ResponseEntity<ErrorResponseDTO> handleIncorrectDateFormatException(IncorrectDateFormatException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
