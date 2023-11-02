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
    public ResponseEntity<String> processErrorBookNotFound(BookNotFoundException bookNotFoundEx){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: " + bookNotFoundEx.getMessage());
    }
    @ExceptionHandler(BookAlreadyExistsException.class)
    public ResponseEntity<String> processBookAlreadyExistsException(BookAlreadyExistsException bookAlreadyExistsException){
        return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: " + bookAlreadyExistsException.getMessage());
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
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<String> handleResourceAlreadyExistsException(ResourceAlreadyExistsException e) {
        return ResponseEntity.status(409).body(e.getMessage());
    }
    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleS3Exception(S3Exception e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    @ExceptionHandler(MultipartFileToFileConversionException.class)
    public ResponseEntity<ErrorResponseDTO> handleMultipartFileToFileConversionException(MultipartFileToFileConversionException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
