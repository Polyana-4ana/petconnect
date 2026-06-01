package com.example.petconnect.service;

import com.example.petconnect.dto.pet.*;
import com.example.petconnect.entity.Pet;
import com.example.petconnect.entity.enums.StatusPet;
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
        pet.setStatus(StatusPet.DISPONIVEL);

        return toResponseDTO(repository.save(pet));
    }

    @Override
    public List<PetResponseDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PetResponseDTO buscarPorId(Long id) {
        Pet pet = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet não encontrado"));

        return toResponseDTO(pet);
    }

    @Override
    public PetResponseDTO atualizar(Long id, PetRequestDTO dto) {
        Pet pet = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet não encontrado"));

        pet.setNome(dto.getNome());
        pet.setIdade(dto.getIdade());
        pet.setEspecie(dto.getEspecie());

        return toResponseDTO(repository.save(pet));
    }

    @Override
    public void deletar(Long id) {
        Pet pet = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet não encontrado"));

        repository.delete(pet);
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
