package com.mercadolibro.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class S3Exception extends RuntimeException {
    private final int code;

    public S3Exception(String message) {
        super(message);
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value(); // Internal server error by default
    }

    public S3Exception(String message, int code) {
        super(message);
        this.code = code;
    }
}
