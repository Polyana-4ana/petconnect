package com.example.petconnect.exception;

public class AdocaoNaoEncontradaException
        extends RuntimeException {

    public AdocaoNaoEncontradaException(Long id) {

        super("Adoção com ID "
                + id
                + " não foi encontrada");
    }
}