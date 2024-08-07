package com.amigoscode.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateResourceFoundException extends RuntimeException{
    public DuplicateResourceFoundException(String message) {
        super(message);
    }
}
