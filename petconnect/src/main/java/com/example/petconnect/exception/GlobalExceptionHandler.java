package com.example.petconnect.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PetNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse tratarPetNaoEncontrado(
            PetNaoEncontradoException ex) {

        return new ErrorResponse(
                LocalDateTime.now(),
                404,
                "NOT_FOUND",
                ex.getMessage()
        );
    }

    @ExceptionHandler(
            AdotanteNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse tratarAdotanteNaoEncontrado(
            AdotanteNaoEncontradoException ex) {

        return new ErrorResponse(
                LocalDateTime.now(),
                404,
                "NOT_FOUND",
                ex.getMessage()
        );
    }

    @ExceptionHandler(
            AdocaoNaoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse tratarAdocaoNaoEncontrada(
            AdocaoNaoEncontradaException ex) {

        return new ErrorResponse(
                LocalDateTime.now(),
                404,
                "NOT_FOUND",
                ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse tratarValidacao(
            MethodArgumentNotValidException ex) {

        String mensagem = ex.getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        return new ErrorResponse(
                LocalDateTime.now(),
                400,
                "VALIDATION_ERROR",
                mensagem
        );
    }

    @ExceptionHandler(
            IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse tratarRegraNegocio(
            IllegalArgumentException ex) {

        return new ErrorResponse(
                LocalDateTime.now(),
                400,
                "BAD_REQUEST",
                ex.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(
            HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse tratarErroGenerico(
            Exception ex) {

        return new ErrorResponse(
                LocalDateTime.now(),
                500,
                "INTERNAL_SERVER_ERROR",
                ex.getMessage()
        );
    }
}