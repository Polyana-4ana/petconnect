package com.example.petconnect.entity;

import com.example.petconnect.entity.enums.StatusAdocao;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "adocoes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "pet_id")
        }
)
public class Adocao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne(optional = false)
    @JoinColumn(name = "adotante_id", nullable = false)
    private Adotante adotante;

    @Column(nullable = false)
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

    public void aprovar() {
        this.status = StatusAdocao.APROVADA;
    }

    public void cancelar() {
        // Correção sutil: Se você mudar o status aqui, não precisa fazer no Service
        this.status = StatusAdocao.CANCELADA;
    }

    // --- MÉTODOS UTILITÁRIOS ---
    public Long getPetId() {
        return this.pet != null ? this.pet.getId() : null;
    }

    // --- GETTERS E SETTERS ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Adotante getAdotante() {
        return adotante;
    }

    public void setAdotante(Adotante adotante) {
        this.adotante = adotante;
    }

    public LocalDateTime getDataAdocao() {
        return dataAdocao;
    }

    public void setDataAdocao(LocalDateTime dataAdocao) {
        this.dataAdocao = dataAdocao;
    }

    public StatusAdocao getStatus() {
        return status;
    }

    public void setStatus(StatusAdocao status) {
        this.status = status;
    }
}