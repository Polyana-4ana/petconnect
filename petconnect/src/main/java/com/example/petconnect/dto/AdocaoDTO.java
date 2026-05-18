package com.petconnect.dto;

public class AdocaoDTO {

    private Long petId;
    private Long adotanteId;

    public AdocaoDTO() {
    }

    public AdocaoDTO(Long petId, Long adotanteId) {
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