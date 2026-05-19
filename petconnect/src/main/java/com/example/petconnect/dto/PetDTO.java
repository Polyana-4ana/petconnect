package com.example.petconnect.dto;

import jakarta.validation.constraints.NotBlank;

public record PetDTO(
    Long id,
    
    @NotBlank(message = "O nome é obrigatório")
    String nome,
    
    boolean adotado,
    
    Integer idade,
    
    @NotBlank(message = "A espécie é obrigatória")
    String especie,
    
    String status
) {}