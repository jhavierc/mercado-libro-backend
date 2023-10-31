package com.mercadolibro.exception;

public class BookNotFoundException extends RuntimeException {
   public BookNotFoundException(String message) {
      super(message);
   }
}
