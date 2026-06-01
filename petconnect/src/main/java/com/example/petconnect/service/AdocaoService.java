package com.example.petconnect.service;

import com.example.petconnect.dto.adocao.*;
import com.example.petconnect.entity.*;
import com.example.petconnect.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Pet não encontrado"));

        Adotante adotante = adotanteRepository.findById(adotanteId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Adotante não encontrado"));

        if (pet.getStatus() != StatusPet.DISPONIVEL) {

            throw new IllegalArgumentException(
                    "Pet indisponível para adoção");
        }

        pet.setStatus(StatusPet.RESERVADO);

        petRepository.save(pet);

        Adocao adocao = new Adocao(
                pet,
                adotante
        );

        adocao.setStatus(
                StatusAdocao.PENDENTE
        );

        return adocaoRepository.save(adocao);
    }

    public Adocao aprovarAdocao(Long id) {

        Adocao adocao = adocaoRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Adoção não encontrada"));

        if (adocao.getStatus() != StatusAdocao.PENDENTE) {

            throw new IllegalArgumentException(
                    "Adoção já finalizada");
        }

        adocao.setStatus(
                StatusAdocao.APROVADA
        );

        Pet pet = adocao.getPet();

        pet.setStatus(
                StatusPet.ADOTADO
        );

        petRepository.save(pet);

        return adocaoRepository.save(adocao);
    }

    public Adocao cancelarAdocao(Long id) {

        Adocao adocao = adocaoRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Adoção não encontrada"));

        if (adocao.getStatus() != StatusAdocao.PENDENTE) {

            throw new IllegalArgumentException(
                    "Adoção já finalizada");
        }

        adocao.setStatus(
                StatusAdocao.CANCELADA
        );

        Pet pet = adocao.getPet();

        pet.setStatus(
                StatusPet.DISPONIVEL
        );

        petRepository.save(pet);

        return adocaoRepository.save(adocao);
    }

    public List<Adocao> listar() {

        return adocaoRepository.findAll();
    }

    public Adocao buscarPorId(Long id) {

        return adocaoRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Adoção não encontrada"));
    }
}