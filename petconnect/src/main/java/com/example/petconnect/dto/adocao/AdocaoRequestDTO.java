package com.example.petconnect.dto.adocao;

import jakarta.validation.constraints.NotNull;

public class AdocaoRequestDTO {

    @NotNull(message = "O id do pet é obrigatório")
    private Long petId;

    @NotNull(message = "O id do adotante é obrigatório")
    private Long adotanteId;

    public AdocaoRequestDTO() {
    }

    public AdocaoRequestDTO(Long petId, Long adotanteId) {
        this.petId = petId;
        this.adotanteId = adotanteId;
    }

    public Long getPetId() {
        return petId;
    }

    public Long getAdotanteId() {
        return adotanteId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public void setAdotanteId(Long adotanteId) {
        this.adotanteId = adotanteId;
    }
}