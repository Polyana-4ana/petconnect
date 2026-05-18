package com.example.petconnect.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "adocoes")
public class Adocao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "adotante_id")
    private Adotante adotante;

    private LocalDateTime dataAdocao;

    @Enumerated(EnumType.STRING)
    private StatusAdocao status;

    public Adocao() {}

    public Adocao(Pet pet, Adotante adotante) {
        this.pet = pet;
        this.adotante = adotante;
    }

    @PrePersist
    public void prePersist() {
        this.dataAdocao = LocalDateTime.now();
        this.status = StatusAdocao.PENDENTE;
    }

    public Long getId() {
        return id;
    }

    public Pet getPet() {
        return pet;
    }

    public Adotante getAdotante() {
        return adotante;
    }

    public LocalDateTime getDataAdocao() {
        return dataAdocao;
    }

    public StatusAdocao getStatus() {
        return status;
    }

    public void setStatus(StatusAdocao status) {
        this.status = status;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public void setAdotante(Adotante adotante) {
        this.adotante = adotante;
    }
}