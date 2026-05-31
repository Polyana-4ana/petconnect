package com.example.petconnect.service;

import com.example.petconnect.dto.pet.*;
import java.util.List;

public interface PetService {
    PetResponseDTO salvar(PetRequestDTO dto);

    List<PetResponseDTO> listar();
}