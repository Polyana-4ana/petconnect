package com.example.petconnect.service;

import com.example.petconnect.dto.adocao.*;
import com.example.petconnect.entity.*;
import com.example.petconnect.entity.enums.StatusAdocao;
import com.example.petconnect.entity.enums.StatusPet;
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

        public AdocaoResponseDTO criarAdocao(AdocaoRequestDTO dto) {

                Pet pet = petRepository.findById(dto.getPetId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Pet não encontrado"));

                Adotante adotante = adotanteRepository.findById(dto.getAdotanteId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Adotante não encontrado"));

                if (pet.getStatus() != StatusPet.DISPONIVEL) {

                        throw new IllegalArgumentException(
                                        "Pet indisponível para adoção");
                }

                pet.setStatus(StatusPet.RESERVADO);

                petRepository.save(pet);

                Adocao adocao = new Adocao(
                                pet,
                                adotante);

                adocao.setStatus(
                                StatusAdocao.PENDENTE);

                Adocao adocaoSalva = adocaoRepository.save(adocao);
                return convertToDTO(adocaoSalva);
        }

        public AdocaoResponseDTO aprovarAdocao(Long id) {

                Adocao adocao = adocaoRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Adoção não encontrada"));

                if (adocao.getStatus() != StatusAdocao.PENDENTE) {

                        throw new IllegalArgumentException(
                                        "Adoção já finalizada");
                }

                adocao.setStatus(
                                StatusAdocao.APROVADA);

                Pet pet = adocao.getPet();

                pet.setStatus(
                                StatusPet.ADOTADO);

                petRepository.save(pet);

                Adocao adocaoSalva = adocaoRepository.save(adocao);
                return convertToDTO(adocaoSalva);
        }

        public AdocaoResponseDTO cancelarAdocao(Long id) {

                Adocao adocao = adocaoRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Adoção não encontrada"));

                if (adocao.getStatus() != StatusAdocao.PENDENTE) {

                        throw new IllegalArgumentException(
                                        "Adoção já finalizada");
                }

                adocao.setStatus(
                                StatusAdocao.CANCELADA);

                Pet pet = adocao.getPet();

                pet.setStatus(
                                StatusPet.DISPONIVEL);

                petRepository.save(pet);

                Adocao adocaoSalva = adocaoRepository.save(adocao);
                return convertToDTO(adocaoSalva);
        }

        public List<Adocao> listar() {

                return adocaoRepository.findAll();
        }

        public Adocao buscarPorId(Long id) {

                return adocaoRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Adoção não encontrada"));
        }

        private AdocaoResponseDTO convertToDTO(Adocao adocao) {

                AdocaoResponseDTO response = new AdocaoResponseDTO();
                response.setId(adocao.getId());
                response.setPetId(adocao.getPet().getId());
                response.setAdotanteId(adocao.getAdotante().getId());
                response.setStatus(adocao.getStatus());
                response.setDataAdocao(adocao.getDataAdocao());
                return response;
        }
}