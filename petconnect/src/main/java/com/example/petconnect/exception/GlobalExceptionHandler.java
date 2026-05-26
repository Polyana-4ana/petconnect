package com.example.petconnect.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            IllegalArgumentException.class)

    @ResponseStatus(
            HttpStatus.BAD_REQUEST)

    public ErrorResponse tratarErro(
            IllegalArgumentException ex){

        return new ErrorResponse(
                LocalDateTime.now(),
                400,
                ex.getMessage()
        );
    }

}