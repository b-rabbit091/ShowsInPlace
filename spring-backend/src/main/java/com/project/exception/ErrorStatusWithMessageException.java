package com.project.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


public class ErrorStatusWithMessageException extends RuntimeException{
    private HttpStatus status;
    private String message;


    @Override
    public String toString() {
        return
                " Message='" + message + '\'' +
                '}';
    }

    public ErrorStatusWithMessageException(HttpStatus status, String errorMessage) {
        this.status=status;
        this.message=errorMessage;
    }
}
