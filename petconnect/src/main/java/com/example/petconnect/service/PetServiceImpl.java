package com.petconnect.service;

import com.petconnect.entity.Pet;
import com.petconnect.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository repository;

    @Override
    public Pet salvar(Pet pet) {

        if (pet.getNome() == null || pet.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome obrigatório");
        }

        pet.setStatus("DISPONIVEL");

        return repository.save(pet);
    }

    @Override
    public List<Pet> listar() {
        return repository.findAll();
    }
}