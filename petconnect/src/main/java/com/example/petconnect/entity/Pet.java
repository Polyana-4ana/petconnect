package com.example.petconnect.entity;

import com.example.petconnect.entity.enums.StatusPet;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "pets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;
    private boolean adotado;

    private Integer idade;

    @NotBlank
    private String especie;

    @Enumerated(EnumType.STRING)
    private StatusPet status;

    public boolean isAdotado() {
        return adotado;
    }

    public void setAdotado(boolean adotado) {
        this.adotado = adotado;
    }
}