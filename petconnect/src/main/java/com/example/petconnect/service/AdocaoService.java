package com.example.petconnect.service;

import com.example.petconnect.dto.adocao.*;
import com.example.petconnect.entity.*;
import com.example.petconnect.entity.enums.StatusAdocao;
import com.example.petconnect.entity.enums.StatusPet;
import com.example.petconnect.exception.AdocaoNaoEncontradaException;
import com.example.petconnect.exception.AdotanteNaoEncontradoException;
import com.example.petconnect.exception.PetNaoEncontradoException;
import com.example.petconnect.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
                                .orElseThrow(() -> new PetNaoEncontradoException(
                                                dto.getPetId()));

                Adotante adotante = adotanteRepository.findById(dto.getAdotanteId())
                                .orElseThrow(() -> new AdotanteNaoEncontradoException(
                                                dto.getAdotanteId()));

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
                                .orElseThrow(() -> new AdocaoNaoEncontradaException(
                                                id));

                if (adocao.getStatus() != StatusAdocao.PENDENTE) {

                        throw new IllegalArgumentException(
                                        "Adoção já finalizada");
                }

                adocao.aprovar();

                Pet pet = adocao.getPet();
                pet.setStatus(StatusPet.ADOTADO);
                petRepository.save(pet);

                Adocao adocaoSalva = adocaoRepository.save(adocao);
                return convertToDTO(adocaoSalva);
        }

        public AdocaoResponseDTO cancelarAdocao(Long id) {

                Adocao adocao = adocaoRepository.findById(id)
                                .orElseThrow(() -> new AdocaoNaoEncontradaException(id));

                if (adocao.getStatus() != StatusAdocao.PENDENTE) {

                        throw new IllegalArgumentException(
                                        "Adoção já finalizada");
                }

                adocao.cancelar();

                Pet pet = adocao.getPet();
                pet.setStatus(StatusPet.DISPONIVEL);
                petRepository.save(pet);

                Adocao adocaoSalva = adocaoRepository.save(adocao);
                return convertToDTO(adocaoSalva);
        }

        public List<AdocaoResponseDTO> listar() {

                return adocaoRepository.findAll()
                                .stream()
                                .map(this::convertToDTO)
                                .collect(Collectors.toList());
        }

        // ALTERADO: agora retorna AdocaoResponseDTO em vez de Adocao (consistência)
        public AdocaoResponseDTO buscarPorId(Long id) {

                Adocao adocao = adocaoRepository.findById(id)
                                .orElseThrow(() -> new AdocaoNaoEncontradaException(id));

                return convertToDTO(adocao);
        }

        private AdocaoResponseDTO convertToDTO(Adocao adocao) {

                AdocaoResponseDTO response = new AdocaoResponseDTO();
                response.setId(adocao.getId());
                response.setPetId(adocao.getPetId());
                response.setNomePet(adocao.getPet().getNome());
                response.setAdotanteId(adocao.getAdotante().getId());
                response.setNomeAdotante(adocao.getAdotante().getNome());
                response.setStatus(adocao.getStatus());
                response.setDataAdocao(adocao.getDataAdocao());
                return response;
        }
}