package com.mercadolibro.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptions {
   @ExceptionHandler(ResourceNotFoundException.class)
   public ResponseEntity<String> processErrorResourceNotFound(ResourceNotFoundException resNotFoundEx){
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: "+ resNotFoundEx.getMessage());
   }
   @ExceptionHandler(BadRequestException.class)
   public ResponseEntity<String> processErrorBadRequest(BadRequestException badReqEx){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR: "+ badReqEx.getMessage());
   }
   @ExceptionHandler(MethodArgumentNotValidException.class) @ResponseBody
   public ResponseEntity<List> processUnmergeException(final MethodArgumentNotValidException ex) {
      List list = ex.getBindingResult().getAllErrors().stream()
              .map(DefaultMessageSourceResolvable::getDefaultMessage)
              .collect(Collectors.toList());
      return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
   }
}
