package com.example.petconnect.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
}