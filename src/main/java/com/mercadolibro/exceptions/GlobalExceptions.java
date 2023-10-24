package com.mercadolibro.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;
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
    @ExceptionHandler(MethodArgumentNotValidException.class) @ResponseBody
    public ResponseEntity<List> processUnmergeException(final MethodArgumentNotValidException ex) {
        List list = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
    }
}
