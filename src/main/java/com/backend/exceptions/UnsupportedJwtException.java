package com.backend.exceptions;

public class UnsupportedJwtException extends RuntimeException{
    public UnsupportedJwtException() {
    }

    public UnsupportedJwtException(String message) {
        super(message);
    }

    public UnsupportedJwtException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedJwtException(Throwable cause) {
        super(cause);
    }
}
