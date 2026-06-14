package com.example.petconnect.service;

import com.example.petconnect.dto.pet.PetRequestDTO;
import com.example.petconnect.dto.pet.PetResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.petconnect.entity.enums.StatusPet;

import java.util.List;

public interface PetService {

    PetResponseDTO salvar(PetRequestDTO dto);

    List<PetResponseDTO> listar();

    PetResponseDTO buscarPorId(Long id);

    PetResponseDTO atualizar(Long id, PetRequestDTO dto);

    void deletar(Long id);

    Page<PetResponseDTO> buscarComFiltro(
            String nome,
            String especie,
            Integer idadeMin,
            Integer idadeMax,
            StatusPet status,
            Pageable pageable
    );
}