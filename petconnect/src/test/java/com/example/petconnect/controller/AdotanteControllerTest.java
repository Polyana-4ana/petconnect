package com.example.petconnect.controller;

import com.example.petconnect.dto.adotante.AdotanteRequestDTO;
import com.example.petconnect.dto.adotante.AdotanteResponseDTO;
import com.example.petconnect.service.AdotanteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdotanteControllerTest {

    private AdotanteController controller;
    private AdotanteServiceFake serviceFake;

    @BeforeEach
    void setUp() {

        serviceFake = new AdotanteServiceFake();
        controller = new AdotanteController(serviceFake);
    }

    @Test
    @DisplayName("Deve cadastrar um adotante com sucesso e retornar Status 201 (Created)")
    void deveCadastrarAdotante() {
        // Arrange
        AdotanteRequestDTO request = new AdotanteRequestDTO();
        request.setNome("João");
        request.setEmail("joao@email.com");
        request.setTelefone("11999999999");

        // Act
        ResponseEntity<AdotanteResponseDTO> resultado = controller.cadastrar(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals(1L, resultado.getBody().getId());
        assertEquals("João", resultado.getBody().getNome());
        assertEquals("joao@email.com", resultado.getBody().getEmail());
        assertEquals("11999999999", resultado.getBody().getTelefone());
    }

    @Test
    @DisplayName("Deve listar todos os adotantes e retornar Status 200 (OK)")
    void deveListarAdotantes() {
        // Act
        ResponseEntity<List<AdotanteResponseDTO>> resultado = controller.listar();

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals(1, resultado.getBody().size());
        assertEquals(1L, resultado.getBody().get(0).getId());
        assertEquals("João", resultado.getBody().get(0).getNome());
    }

    @Test
    @DisplayName("Deve buscar um adotante por ID e retornar Status 200 (OK)")
    void deveBuscarAdotantePorId() {
        // Act
        ResponseEntity<AdotanteResponseDTO> resultado = controller.buscarPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals(1L, resultado.getBody().getId());
        assertEquals("João", resultado.getBody().getNome());
    }

    @Test
    @DisplayName("Deve atualizar dados do adotante e retornar Status 200 (OK)")
    void deveAtualizarAdotante() {
        // Arrange
        AdotanteRequestDTO request = new AdotanteRequestDTO();
        request.setNome("Maria");
        request.setEmail("maria@email.com");
        request.setTelefone("11888888888");

        // Act
        ResponseEntity<AdotanteResponseDTO> resultado = controller.atualizar(1L, request);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals(1L, resultado.getBody().getId());
        assertEquals("Maria", resultado.getBody().getNome());
        assertEquals("maria@email.com", resultado.getBody().getEmail());
        assertEquals("11888888888", resultado.getBody().getTelefone());
    }

    @Test
    @DisplayName("Deve deletar um adotante com sucesso e retornar Status 204 (No Content)")
    void deveDeletarAdotante() {
        // Act
        ResponseEntity<Void> resultado = controller.deletar(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        assertNull(resultado.getBody());
    }

    private static class AdotanteServiceFake extends AdotanteService {

        public AdotanteServiceFake() {

            super(null);
        }

        @Override
        public AdotanteResponseDTO cadastrar(AdotanteRequestDTO dto) {
            AdotanteResponseDTO response = new AdotanteResponseDTO(
                    1L,
                    dto.getNome(),
                    dto.getEmail(),
                    dto.getTelefone()
            );
            return response;
        }

        @Override
        public List<AdotanteResponseDTO> listar() {
            AdotanteResponseDTO response = new AdotanteResponseDTO(
                    1L,
                    "João",
                    "joao@email.com",
                    "11999999999"
            );
            return List.of(response);
        }

        @Override
        public AdotanteResponseDTO buscarPorId(Long id) {
            AdotanteResponseDTO response = new AdotanteResponseDTO(
                    id,
                    "João",
                    "joao@email.com",
                    "11999999999"
            );
            return response;
        }

        @Override
        public AdotanteResponseDTO atualizar(Long id, AdotanteRequestDTO dto) {
            AdotanteResponseDTO response = new AdotanteResponseDTO(
                    id,
                    dto.getNome(),
                    dto.getEmail(),
                    dto.getTelefone()
            );
            return response;
        }

        @Override
        public void deletar(Long id) {

        }
    }
}
