package com.petconnect.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> tratarErro(IllegalArgumentException ex) {

        return Map.of(
                "erro", ex.getMessage()
        );
    }
}