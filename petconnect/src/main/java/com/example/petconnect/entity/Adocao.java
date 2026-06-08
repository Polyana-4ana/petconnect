package com.example.petconnect.entity;

import com.example.petconnect.entity.Adotante;
import com.example.petconnect.entity.Pet;
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
        this.status = StatusAdocao.CANCELADA;
    }

}