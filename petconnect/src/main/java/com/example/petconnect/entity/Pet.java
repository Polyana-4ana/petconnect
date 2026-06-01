package com.example.petconnect.entity;

import com.example.petconnect.entity.enums.StatusPet;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Min(value = 0, message = "Idade inválida")
    private Integer idade;

    @NotBlank(message = "Espécie é obrigatória")
    private String especie;

    @Enumerated(EnumType.STRING)
    private StatusPet status;

    public boolean isAdotado() {
        return status == StatusPet.ADOTADO;
    }

    public void setAdotado(boolean adotado) {
        this.status = adotado ? StatusPet.ADOTADO : StatusPet.DISPONIVEL;
    }
}