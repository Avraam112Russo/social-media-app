package com.n1nt3nd0.videoservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler
    public ResponseEntity<ResponseError> handleDownloadVideoException(DownloadVideoFromGcsException exception){
        ResponseError responseError = new ResponseError();
        responseError.setMessage(exception.getMessage());
        responseError.setHttpStatus(exception.getHttpStatus());
        return new ResponseEntity<>(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
