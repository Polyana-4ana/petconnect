package com.example.petconnect.controller;

import com.example.petconnect.dto.adocao.AdocaoRequestDTO;
import com.example.petconnect.dto.adocao.AdocaoResponseDTO;
import com.example.petconnect.service.AdocaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class AdocaoControllerTest {

    private AdocaoController controller;
    private AdocaoServiceFake serviceFake;

    @BeforeEach
    void setUp() {
        serviceFake = new AdocaoServiceFake();
        controller = new AdocaoController(serviceFake);
    }

    @Test
    @DisplayName("Deve criar uma adoção com sucesso e retornar Status 201 (Created)")
    void deveCriarAdocao() {
        // Arrange
        AdocaoRequestDTO request = new AdocaoRequestDTO();
        request.setPetId(1L);
        request.setAdotanteId(2L);

        // Act
        ResponseEntity<AdocaoResponseDTO> resultado = controller.criar(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals(1L, resultado.getBody().getId());
        assertEquals(1L, resultado.getBody().getPetId());
        assertEquals(2L, resultado.getBody().getAdotanteId());

        assertNotNull(resultado.getBody().getStatus());
    }

    @Test
    @DisplayName("Deve aprovar uma adoção e retornar Status 200 (OK)")
    void deveAprovarAdocao() {
        // Act
        ResponseEntity<AdocaoResponseDTO> resultado = controller.aprovar(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals(1L, resultado.getBody().getId());
    }

    @Test
    @DisplayName("Deve cancelar uma adoção e retornar Status 200 (OK)")
    void deveCancelarAdocao() {
        // Act
        ResponseEntity<AdocaoResponseDTO> resultado = controller.cancelar(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals(1L, resultado.getBody().getId());
    }

    // CLASSE FAKE (Substitui os Repositories @Autowired e o Mockito)

    private static class AdocaoServiceFake extends AdocaoService {

        public AdocaoServiceFake() {
            super();
        }

        @Override
        public AdocaoResponseDTO criarAdocao(AdocaoRequestDTO dto) {
            AdocaoResponseDTO response = new AdocaoResponseDTO();
            response.setId(1L);
            response.setPetId(dto.getPetId());
            response.setAdotanteId(dto.getAdotanteId());

            // CORREÇÃO: Definindo o status para não vir nulo!
            // Se o setStatus da sua DTO pedir um Enum (StatusAdocao), mude para StatusAdocao.PENDENTE
            response.setStatus(com.example.petconnect.entity.enums.StatusAdocao.PENDENTE);

            return response;
        }

        @Override
        public AdocaoResponseDTO aprovarAdocao(Long id) {
            AdocaoResponseDTO response = new AdocaoResponseDTO();
            response.setId(id);
            response.setStatus(com.example.petconnect.entity.enums.StatusAdocao.APROVADA);
            return response;
        }

        @Override
        public AdocaoResponseDTO cancelarAdocao(Long id) {
            AdocaoResponseDTO response = new AdocaoResponseDTO();
            response.setId(id);
            response.setStatus(com.example.petconnect.entity.enums.StatusAdocao.CANCELADA);
            return response;
        }
    }


}