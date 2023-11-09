package com.mercadolibro.exception;

public class BookImageKeyDoesNotExistException extends RuntimeException {
    public BookImageKeyDoesNotExistException(String message) {
        super(message);
    }
}
