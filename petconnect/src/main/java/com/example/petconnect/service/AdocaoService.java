package com.example.petconnect.service;

import com.example.petconnect.entity.*;
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

    public Adocao criarAdocao(Long petId, Long adotanteId) {

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet não encontrado"));

        Adotante adotante = adotanteRepository.findById(adotanteId)
                .orElseThrow(() -> new RuntimeException("Adotante não encontrado"));

        if (pet.isAdotado()) {
            throw new RuntimeException("Este pet já foi adotado");
        }

        Adocao adocao = new Adocao(pet, adotante);

        pet.setAdotado(true);
        petRepository.save(pet);

        return adocaoRepository.save(adocao);
    }

    public Adocao aprovarAdocao(Long id) {
        Adocao adocao = adocaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoção não encontrada"));

        adocao.setStatus(StatusAdocao.APROVADA);

        return adocaoRepository.save(adocao);
    }

    public Adocao cancelarAdocao(Long id) {
        Adocao adocao = adocaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoção não encontrada"));

        adocao.setStatus(StatusAdocao.CANCELADA);

        Pet pet = adocao.getPet();
        pet.setAdotado(false);
        petRepository.save(pet);

        return adocaoRepository.save(adocao);
    }
}