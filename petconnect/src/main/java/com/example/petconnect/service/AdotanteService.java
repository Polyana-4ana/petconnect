package com.example.petconnect.service;

import com.example.petconnect.dto.adotante.*;
import com.example.petconnect.entity.Adotante;
import com.example.petconnect.repository.AdotanteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdotanteService {

    private final AdotanteRepository repository;

    public AdotanteService(AdotanteRepository repository) {
        this.repository = repository;
    }

    public AdotanteResponseDTO cadastrar(AdotanteRequestDTO dto) {
        repository.findByEmail(dto.getEmail())
                .ifPresent(a -> {
                    throw new IllegalArgumentException("Email já cadastrado");
                });

        Adotante adotante = new Adotante();
        adotante.setNome(dto.getNome());
        adotante.setEmail(dto.getEmail());
        adotante.setTelefone(dto.getTelefone());

        return toResponseDTO(repository.save(adotante));
    }

    public List<AdotanteResponseDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public AdotanteResponseDTO buscarPorId(Long id) {
        Adotante adotante = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Adotante não encontrado"));

        return toResponseDTO(adotante);
    }

    public AdotanteResponseDTO atualizar(Long id, AdotanteRequestDTO dto) {
        Adotante adotante = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Adotante não encontrado"));

        adotante.setNome(dto.getNome());
        adotante.setEmail(dto.getEmail());
        adotante.setTelefone(dto.getTelefone());

        return toResponseDTO(repository.save(adotante));
    }

    public void deletar(Long id) {
        Adotante adotante = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Adotante não encontrado"));

        repository.delete(adotante);
    }

    private AdotanteResponseDTO toResponseDTO(Adotante adotante) {
        return new AdotanteResponseDTO(
                adotante.getId(),
                adotante.getNome(),
                adotante.getEmail(),
                adotante.getTelefone());
    }
}