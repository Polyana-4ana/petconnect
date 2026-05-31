package com.example.petconnect.service;

import com.example.petconnect.dto.pet.*;
import com.example.petconnect.entity.Pet;
import com.example.petconnect.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository repository;

    @Override
    public PetResponseDTO salvar(PetRequestDTO dto) {
        Pet pet = new Pet();
        pet.setNome(dto.getNome());
        pet.setIdade(dto.getIdade());
        pet.setEspecie(dto.getEspecie());
        pet.setStatus("DISPONIVEL");

        return toResponseDTO(repository.save(pet));
    }

    @Override
    public List<PetResponseDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private PetResponseDTO toResponseDTO(Pet pet) {
        return new PetResponseDTO(
                pet.getId(),
                pet.getNome(),
                pet.isAdotado(),
                pet.getIdade(),
                pet.getEspecie(),
                pet.getStatus());
    }
}