package com.example.petconnect.dto.adocao;

import com.example.petconnect.entity.StatusAdocao;
import java.time.LocalDateTime;

public class AdocaoResponseDTO {
    private Long id;
    private Long petId;
    private String nomePet;
    private Long adotanteId;
    private String nomeAdotante;
    private LocalDateTime dataAdocao;
    private StatusAdocao status;

    public AdocaoResponseDTO() {
    }

    public AdocaoResponseDTO(Long id, Long petId, String nomePet,
            Long adotanteId, String nomeAdotante,
            LocalDateTime dataAdocao, StatusAdocao status) {
        this.id = id;
        this.petId = petId;
        this.nomePet = nomePet;
        this.adotanteId = adotanteId;
        this.nomeAdotante = nomeAdotante;
        this.dataAdocao = dataAdocao;
        this.status = status;

    }

    public Long getId() {
        return id;
    }

    public Long getPetId() {
        return petId;
    }

    public String getNomePet() {
        return nomePet;
    }

    public Long getAdotanteId() {
        return adotanteId;
    }

    public String getNomeAdotante() {
        return nomeAdotante;
    }

    public LocalDateTime getDataAdocao() {
        return dataAdocao;
    }

    public StatusAdocao getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public void setNomePet(String nomePet) {
        this.nomePet = nomePet;
    }

    public void setAdotanteId(Long adotanteId) {
        this.adotanteId = adotanteId;
    }

    public void setNomeAdotante(String nomeAdotante) {
        this.nomeAdotante = nomeAdotante;
    }

    public void setDataAdocao(LocalDateTime dataAdocao) {
        this.dataAdocao = dataAdocao;
    }

    public void setStatus(StatusAdocao status) {
        this.status = status;
    }

}
