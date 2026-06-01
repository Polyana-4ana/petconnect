package com.example.petconnect.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String telefone;
}