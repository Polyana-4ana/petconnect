package com.petconnect.entity;

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

    private Integer idade;

    @NotBlank
    private String especie;

    private String status;
}