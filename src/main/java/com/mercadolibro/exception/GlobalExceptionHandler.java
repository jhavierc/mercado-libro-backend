package com.mercadolibro.exception;

import com.mercadolibro.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoBooksToShowException.class)
    public ResponseEntity<List<?>> handleErrorNoBooksToShow() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
    }

    @ExceptionHandler(NoPagesException.class)
    public ResponseEntity<Long> handleNoPages() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0L);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleErrorBookNotFound(BookNotFoundException bookNotFoundEx){
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(bookNotFoundEx.getMessage(), HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BookAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBookAlreadyExistsException(BookAlreadyExistsException bookAlreadyExistsException){
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(bookAlreadyExistsException.getMessage(), HttpStatus.CONFLICT.value());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleErrorCategoryNotFound(CategoryNotFoundException categoryNotFoundException){
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(categoryNotFoundException.getMessage(), HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
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

        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
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

    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleS3Exception(S3Exception e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage(), e.getCode());

        return ResponseEntity.status(e.getCode()).body(errorResponse);
    }

    @ExceptionHandler(MultipartFileToDTOConversionException.class)
    public ResponseEntity<ErrorResponseDTO> handleMultipartFileToFileConversionException(MultipartFileToDTOConversionException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(BookImageKeyDoesNotExistException.class)
    public ResponseEntity<ErrorResponseDTO> handleBookImageKeyDoesNotExist(BookImageKeyDoesNotExistException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage(), HttpStatus.CONFLICT.value());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(UnsupportedParameterTypeException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnsupportedParameterTypeException(UnsupportedParameterTypeException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedException(UnauthorizedException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage(), HttpStatus.UNAUTHORIZED.value());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleImageNotFoundException(ImageNotFoundException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage(), HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BookImageLimitException.class)
    public ResponseEntity<ErrorResponseDTO> handleBookImageLimitException(BookImageLimitException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage(), HttpStatus.CONFLICT.value());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(InvoicePaymentException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvoicePaymentException(InvoicePaymentException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(e.getMessage(), e.getCode());

        return ResponseEntity.status(e.getCode()).body(errorResponse);
    }
}
