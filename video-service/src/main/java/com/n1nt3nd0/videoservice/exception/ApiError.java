package com.n1nt3nd0.videoservice.exception;

public abstract class ApiError extends RuntimeException{
    public ApiError(String message) {
        super(message);
    }

    public ApiError(String message, Throwable cause) {
        super(message, cause);
    }
}
