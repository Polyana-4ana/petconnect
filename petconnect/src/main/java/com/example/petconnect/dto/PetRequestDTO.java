package com.example.petconnect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class PetRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @Positive(message = "A idade deve ser positiva")
    private Integer idade;

    @NotBlank(message = "A espécie é obrigatória")
    private String especie;

    public PetRequestDTO() {
    }

    public PetRequestDTO(String nome, Integer idade, String especie) {
        this.nome = nome;
        this.idade = idade;
        this.especie = especie;
    }

    public String getNome() {
        return nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public String getEspecie() {
        return especie;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }
}