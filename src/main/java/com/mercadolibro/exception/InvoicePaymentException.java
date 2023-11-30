package com.mercadolibro.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvoicePaymentException extends RuntimeException{
    private final int code;
    private final String description;


    public InvoicePaymentException(String message) {
        super(message);
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value(); // Internal server error by default
        this.description = "";
    }

    public InvoicePaymentException(String message, int code) {
        super(message);
        this.code = code;
        this.description = "";
    }

    public InvoicePaymentException(String message, String description, int code) {
        super(message);
        this.description = description;
        this.code = code;
    }
}
