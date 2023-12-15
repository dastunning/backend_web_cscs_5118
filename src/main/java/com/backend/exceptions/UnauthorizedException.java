package com.backend.exceptions;

public class UnauthorizedException extends RuntimeException {
    private final int statusCode;

    public UnauthorizedException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
