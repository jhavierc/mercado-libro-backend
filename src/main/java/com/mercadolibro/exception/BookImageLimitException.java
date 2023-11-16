package com.mercadolibro.exception;

public class BookImageLimitException extends RuntimeException {
    public BookImageLimitException(String message) {
        super(message);
    }
}
