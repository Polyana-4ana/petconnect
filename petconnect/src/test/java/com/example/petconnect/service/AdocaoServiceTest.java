package com.example.petconnect.service;

import com.example.petconnect.dto.adocao.AdocaoRequestDTO;
import com.example.petconnect.dto.adocao.AdocaoResponseDTO;
import com.example.petconnect.entity.Adocao;
import com.example.petconnect.entity.Adotante;
import com.example.petconnect.entity.Pet;
import com.example.petconnect.entity.enums.StatusAdocao;
import com.example.petconnect.entity.enums.StatusPet;
import com.example.petconnect.exception.AdocaoNaoEncontradaException;
import com.example.petconnect.repository.AdocaoRepository;
import com.example.petconnect.repository.AdotanteRepository;
import com.example.petconnect.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdocaoServiceTest {

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private AdotanteRepository adotanteRepository;

    @InjectMocks
    private AdocaoService adocaoService;

    private Pet petDisponivel;
    private Pet petIndisponivel;
    private Adotante adotantePadrao;
    private Adocao adocaoPendente;

    @BeforeEach
    void setUp() {
        // Inicialização do Pet Disponível
        petDisponivel = new Pet();
        petDisponivel.setId(1L);
        petDisponivel.setNome("Thor");
        petDisponivel.setStatus(StatusPet.DISPONIVEL);

        // Inicialização do Pet Indisponível
        petIndisponivel = new Pet();
        petIndisponivel.setId(2L);
        petIndisponivel.setNome("Mel");
        petIndisponivel.setStatus(StatusPet.ADOTADO);

        // Inicialização do Adotante Padrão
        adotantePadrao = new Adotante();
        adotantePadrao.setId(1L);
        adotantePadrao.setNome("João Silva");

        // Inicialização de uma Adoção Padrão (Status PENDENTE)
        adocaoPendente = new Adocao(petDisponivel, adotantePadrao);
        adocaoPendente.setId(10L);
        adocaoPendente.setStatus(StatusAdocao.PENDENTE);
        adocaoPendente.setDataAdocao(LocalDateTime.now());
    }

    @Test
    @DisplayName("Criar adoção com sucesso quando Pet está DISPONIVEL e Adotante existe")
    void criarAdocao_ComSucesso() {

        AdocaoRequestDTO requestDTO = new AdocaoRequestDTO(1L, 1L);

        when(petRepository.findById(1L)).thenReturn(Optional.of(petDisponivel));
        when(adotanteRepository.findById(1L)).thenReturn(Optional.of(adotantePadrao));


        when(adocaoRepository.save(any(Adocao.class))).thenAnswer(invocation -> {
            Adocao adocaoArg = invocation.getArgument(0);
            adocaoArg.setId(100L);
            return adocaoArg;
        });


        AdocaoResponseDTO response = adocaoService.criarAdocao(requestDTO);


        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals(StatusPet.RESERVADO, petDisponivel.getStatus());
        assertEquals(StatusAdocao.PENDENTE, response.getStatus());

        verify(petRepository, times(1)).findById(1L);
        verify(adotanteRepository, times(1)).findById(1L);
        verify(petRepository, times(1)).save(petDisponivel);
        verify(adocaoRepository, times(1)).save(any(Adocao.class));
    }

    @Test
    @DisplayName("Lançar exceção ao tentar criar adoção de um Pet que não está DISPONIVEL")
    void criarAdocao_PetIndisponivel_LancaException() {

        AdocaoRequestDTO requestDTO = new AdocaoRequestDTO(2L, 1L);

        when(petRepository.findById(2L)).thenReturn(Optional.of(petIndisponivel));
        when(adotanteRepository.findById(1L)).thenReturn(Optional.of(adotantePadrao));


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adocaoService.criarAdocao(requestDTO);
        });

        assertEquals("Pet indisponível para adoção", exception.getMessage());


        assertEquals(StatusPet.ADOTADO, petIndisponivel.getStatus());

        verify(petRepository, times(1)).findById(2L);
        verify(adotanteRepository, times(1)).findById(1L);
        verify(petRepository, never()).save(any(Pet.class));
        verify(adocaoRepository, never()).save(any(Adocao.class));
    }

    @Test
    @DisplayName("Cancelar uma adoção existente com status PENDENTE")
    void cancelarAdocao_ComSucesso() {

        Long adocaoId = 10L;

        petDisponivel.setStatus(StatusPet.RESERVADO);

        when(adocaoRepository.findById(adocaoId)).thenReturn(Optional.of(adocaoPendente));
        when(adocaoRepository.save(any(Adocao.class))).thenReturn(adocaoPendente);


        AdocaoResponseDTO response = adocaoService.cancelarAdocao(adocaoId);


        assertNotNull(response);
        assertEquals(StatusAdocao.CANCELADA, adocaoPendente.getStatus());
        assertEquals(StatusPet.DISPONIVEL, petDisponivel.getStatus());

        verify(adocaoRepository, times(1)).findById(adocaoId);
        verify(petRepository, times(1)).save(petDisponivel);
        verify(adocaoRepository, times(1)).save(adocaoPendente);
    }

    @Test
    @DisplayName("Aprovar uma adoção existente com status PENDENTE")
    void aprovarAdocao_ComSucesso() {

        Long adocaoId = 10L;
        petDisponivel.setStatus(StatusPet.RESERVADO);

        when(adocaoRepository.findById(adocaoId)).thenReturn(Optional.of(adocaoPendente));
        when(adocaoRepository.save(any(Adocao.class))).thenReturn(adocaoPendente);


        AdocaoResponseDTO response = adocaoService.aprovarAdocao(adocaoId);


        assertNotNull(response);
        assertEquals(StatusAdocao.APROVADA, adocaoPendente.getStatus());
        assertEquals(StatusPet.ADOTADO, petDisponivel.getStatus());

        verify(adocaoRepository, times(1)).findById(adocaoId);
        verify(petRepository, times(1)).save(petDisponivel);
        verify(adocaoRepository, times(1)).save(adocaoPendente);
    }

    @Test
    @DisplayName("Cenário extra - Erro ao tentar Aprovar/Cancelar adoção com status diferente de PENDENTE")
    void alterarAdocao_JaFinalizada_LancaException() {

        Long adocaoId = 10L;
        adocaoPendente.setStatus(StatusAdocao.APROVADA);

        when(adocaoRepository.findById(adocaoId)).thenReturn(Optional.of(adocaoPendente));


        IllegalArgumentException exceptionAprovar = assertThrows(IllegalArgumentException.class, () -> {
            adocaoService.aprovarAdocao(adocaoId);
        });
        assertEquals("Adoção já finalizada", exceptionAprovar.getMessage());


        IllegalArgumentException exceptionCancelar = assertThrows(IllegalArgumentException.class, () -> {
            adocaoService.cancelarAdocao(adocaoId);
        });
        assertEquals("Adoção já finalizada", exceptionCancelar.getMessage());

        verify(petRepository, never()).save(any(Pet.class));
        verify(adocaoRepository, never()).save(any(Adocao.class));
    }
}