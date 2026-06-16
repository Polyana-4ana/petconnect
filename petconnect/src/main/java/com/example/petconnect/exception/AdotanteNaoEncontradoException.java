package com.example.petconnect.exception;

public class AdotanteNaoEncontradoException
        extends RuntimeException {

    public AdotanteNaoEncontradoException(Long id) {

        super("Adotante com ID "
                + id
                + " não foi encontrado");
    }
}