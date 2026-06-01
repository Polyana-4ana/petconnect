package com.example.petconnect.service;

import com.example.petconnect.dto.adocao.*;
import com.example.petconnect.entity.*;
import com.example.petconnect.entity.enums.StatusAdocao;
import com.example.petconnect.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdocaoService {

    @Autowired
    private AdocaoRepository adocaoRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private AdotanteRepository adotanteRepository;

    public AdocaoResponseDTO criarAdocao(AdocaoRequestDTO dto) {

        Pet pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new RuntimeException("Pet não encontrado"));

        Adotante adotante = adotanteRepository.findById(dto.getAdotanteId())
                .orElseThrow(() -> new RuntimeException("Adotante não encontrado"));

        if (pet.isAdotado()) {
            throw new RuntimeException("Este pet já foi adotado");
        }

        Adocao adocao = new Adocao(pet, adotante);

        pet.setAdotado(true);
        petRepository.save(pet);
        Adocao salva = adocaoRepository.save(adocao);

        return toResponseDTO(salva);
    }

    public AdocaoResponseDTO aprovarAdocao(Long id) {
        Adocao adocao = adocaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoção não encontrada"));

        adocao.setStatus(StatusAdocao.APROVADA);

        return toResponseDTO(adocaoRepository.save(adocao));
    }

    public AdocaoResponseDTO cancelarAdocao(Long id) {
        Adocao adocao = adocaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoção não encontrada"));

        adocao.setStatus(StatusAdocao.CANCELADA);

        Pet pet = adocao.getPet();
        pet.setAdotado(false);
        petRepository.save(pet);

        return toResponseDTO(adocaoRepository.save(adocao));
    }

    private AdocaoResponseDTO toResponseDTO(Adocao adocao) {
        return new AdocaoResponseDTO(
                adocao.getId(),
                adocao.getPet().getId(),
                adocao.getPet().getNome(),
                adocao.getAdotante().getId(),
                adocao.getAdotante().getNome(),
                adocao.getDataAdocao(),
                adocao.getStatus());
    }
}