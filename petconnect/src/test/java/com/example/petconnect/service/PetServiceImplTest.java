package com.example.petconnect.service;

import com.example.petconnect.dto.pet.PetRequestDTO;
import com.example.petconnect.dto.pet.PetResponseDTO;
import com.example.petconnect.entity.Pet;
import com.example.petconnect.entity.enums.StatusPet;
import com.example.petconnect.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {

    @Mock
    private PetRepository petRepository;

    // Nota: Altere para a sua classe concreta (ex: PetServiceImpl) se este for o nome real dela
    @InjectMocks
    private PetServiceImpl petService;

    private PetRequestDTO petValidoDTO;

    @BeforeEach
    void setUp() {
        // Dados de entrada construídos para os cenários comuns
        petValidoDTO = new PetRequestDTO("Thor", 3, "Cachorro");
    }

    @Test
    @DisplayName("Pet com nome e espécie válidos")
    void salvar_ComDadosValidos_DeveRetornarPetDisponivel() {
        // Arrange
        Pet petSalvo = Pet.builder()
                .id(1L)
                .nome(petValidoDTO.getNome())
                .idade(petValidoDTO.getIdade())
                .especie(petValidoDTO.getEspecie())
                .status(StatusPet.DISPONIVEL)
                .build();

        when(petRepository.save(any(Pet.class))).thenReturn(petSalvo);


        PetResponseDTO resultado = petService.salvar(petValidoDTO);


        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Thor", resultado.getNome());
        assertEquals(StatusPet.DISPONIVEL, resultado.getStatus());
        assertFalse(resultado.getAdotado());

        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    @DisplayName("Pet com nome nulo")
    void salvar_ComNomeNulo_DeveLancarIllegalArgumentException() {

        PetRequestDTO requestComNomeNulo = new PetRequestDTO(null, 2, "Gato");


        assertThrows(IllegalArgumentException.class, () -> {
            petService.salvar(requestComNomeNulo);
        });

        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    @DisplayName("Pet com nome em branco (\"   \")")
    void salvar_ComNomeEmBranco_DeveLancarIllegalArgumentException() {

        PetRequestDTO requestComNomeVazio = new PetRequestDTO("   ", 2, "Gato");


        assertThrows(IllegalArgumentException.class, () -> {
            petService.salvar(requestComNomeVazio);
        });

        verify(petRepository, never()).save(any(Pet.class));
    }
}