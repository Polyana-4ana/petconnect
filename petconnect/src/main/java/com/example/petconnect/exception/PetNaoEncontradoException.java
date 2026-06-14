package com.example.petconnect.exception;

public class PetNaoEncontradoException
        extends RuntimeException {

    public PetNaoEncontradoException(Long id) {

        super("Pet com ID "
                + id
                + " não foi encontrado");
    }
}