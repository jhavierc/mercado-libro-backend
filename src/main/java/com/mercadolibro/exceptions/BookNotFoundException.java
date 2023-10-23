package com.mercadolibro.exceptions;

public class BookNotFoundException extends RuntimeException {
   public BookNotFoundException(String message) {
      super(message);
   }
}
