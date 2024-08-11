package com.n1nt3nd0.videoservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DownloadVideoFromGcsException extends ApiError{
    private String message;
    private HttpStatus httpStatus;
    public DownloadVideoFromGcsException(String message) {
        super(message);
    }

    public DownloadVideoFromGcsException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
