package com.example.petconnect.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "adotantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Adotante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 10, max = 15)
    @Column(nullable = false)
    private String telefone;

    @NotBlank
    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(nullable = false)
    private LocalDateTime dataCadastro;

    @PrePersist
    public void prePersist() {
        this.dataCadastro = LocalDateTime.now();
    }
}