package com.example.petconnect.controller;

import com.example.petconnect.dto.pet.PetRequestDTO;
import com.example.petconnect.dto.pet.PetResponseDTO;
import com.example.petconnect.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PetControllerTest {

    private PetController controller;
    private PetServiceFake serviceFake;

    @BeforeEach
    void setUp() {
        serviceFake = new PetServiceFake();

        controller = new PetController(serviceFake);
    }

    @Test
    @DisplayName("Deve salvar um pet com sucesso e retornar Status 201 (Created)")
    void deveSalvarPet() {
        // Arrange
        PetRequestDTO request = new PetRequestDTO();
        request.setNome("Rex");
        request.setIdade(3);
        request.setEspecie("Cachorro");

        // Act
        ResponseEntity<PetResponseDTO> resultado = controller.salvar(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals(1L, resultado.getBody().getId());
        assertEquals("Rex", resultado.getBody().getNome());
        assertFalse(resultado.getBody().getAdotado());
        assertEquals(3, resultado.getBody().getIdade());
        assertEquals("Cachorro", resultado.getBody().getEspecie());
        assertNotNull(resultado.getBody().getStatus());
    }

    @Test
    @DisplayName("Deve listar todos os pets e retornar Status 200 (OK)")
    void deveListarPets() {
        // Act
        ResponseEntity<List<PetResponseDTO>> resultado = controller.listar();

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals(1, resultado.getBody().size());
        assertEquals(1L, resultado.getBody().get(0).getId());
        assertEquals("Rex", resultado.getBody().get(0).getNome());
    }

    @Test
    @DisplayName("Deve buscar um pet por ID e retornar Status 200 (OK)")
    void deveBuscarPetPorId() {
        // Act
        ResponseEntity<PetResponseDTO> resultado = controller.buscarPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals(1L, resultado.getBody().getId());
        assertEquals("Rex", resultado.getBody().getNome());
    }

    @Test
    @DisplayName("Deve atualizar dados do pet e retornar Status 200 (OK)")
    void deveAtualizarPet() {
        // Arrange
        PetRequestDTO request = new PetRequestDTO();
        request.setNome("Thor");
        request.setIdade(4);
        request.setEspecie("Cachorro");

        // Act
        ResponseEntity<PetResponseDTO> resultado = controller.atualizar(1L, request);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals(1L, resultado.getBody().getId());
        assertEquals("Thor", resultado.getBody().getNome());
        assertEquals(4, resultado.getBody().getIdade());
    }

    @Test
    @DisplayName("Deve deletar um pet com sucesso e retornar Status 204 (No Content)")
    void deveDeletarPet() {
        // Act
        ResponseEntity<Void> resultado = controller.deletar(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        assertNull(resultado.getBody());
    }


    private static class PetServiceFake implements PetService {

        @Override
        public PetResponseDTO salvar(PetRequestDTO dto) {

            return new PetResponseDTO(
                    1L,
                    dto.getNome(),
                    false,
                    dto.getIdade(),
                    dto.getEspecie(),
                    com.example.petconnect.entity.enums.StatusPet.DISPONIVEL
            );
        }

        @Override
        public List<PetResponseDTO> listar() {
            PetResponseDTO pet = new PetResponseDTO(
                    1L,
                    "Rex",
                    false,
                    3,
                    "Cachorro",
                    com.example.petconnect.entity.enums.StatusPet.DISPONIVEL
            );
            return List.of(pet);
        }

        @Override
        public PetResponseDTO buscarPorId(Long id) {
            return new PetResponseDTO(
                    id,
                    "Rex",
                    false,
                    3,
                    "Cachorro",
                    com.example.petconnect.entity.enums.StatusPet.DISPONIVEL
            );
        }

        @Override
        public PetResponseDTO atualizar(Long id, PetRequestDTO dto) {
            return new PetResponseDTO(
                    id,
                    dto.getNome(),
                    false,
                    dto.getIdade(),
                    dto.getEspecie(),
                    com.example.petconnect.entity.enums.StatusPet.DISPONIVEL
            );
        }

        @Override
        public void deletar(Long id) {
            // Método void: Simula execução de sucesso não fazendo nada
        }
    }
}