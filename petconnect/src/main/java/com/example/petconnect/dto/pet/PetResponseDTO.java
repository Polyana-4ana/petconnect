package com.example.petconnect.dto.pet;

import com.example.petconnect.entity.enums.StatusPet;

public class PetResponseDTO {

    private Long id;
    private String nome;
    private Boolean adotado;
    private Integer idade;
    private String especie;
    private StatusPet status;

    public PetResponseDTO() {
    }

    public PetResponseDTO(Long id, String nome, Boolean adotado,
            Integer idade, String especie, StatusPet status) {
        this.id = id;
        this.nome = nome;
        this.adotado = adotado;
        this.idade = idade;
        this.especie = especie;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Boolean getAdotado() {
        return adotado;
    }

    public Integer getIdade() {
        return idade;
    }

    public String getEspecie() {
        return especie;
    }

    public StatusPet getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setAdotado(Boolean adotado) {
        this.adotado = adotado;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public void setStatus(StatusPet status) {
        this.status = status;
    }
}