package com.example.petconnect.controller;

import com.example.petconnect.dto.adotante.AdotanteRequestDTO;
import com.example.petconnect.dto.adotante.AdotanteResponseDTO;
import com.example.petconnect.service.AdotanteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdotanteController.class)
class AdotanteControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private AdotanteService service;

        @Test
        void deveCadastrarAdotante() throws Exception {

                AdotanteRequestDTO request = new AdotanteRequestDTO();
                request.setNome("João");

                AdotanteResponseDTO response = new AdotanteResponseDTO();
                response.setId(1L);
                response.setNome("João");

                Mockito.when(service.cadastrar(any()))
                                .thenReturn(response);

                mockMvc.perform(post("/adotantes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.nome").value("João"));
        }

        @Test
        void deveListarAdotantes() throws Exception {

                AdotanteResponseDTO response = new AdotanteResponseDTO();
                response.setId(1L);
                response.setNome("João");

                Mockito.when(service.listar())
                                .thenReturn(List.of(response));

                mockMvc.perform(get("/adotantes"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").value(1))
                                .andExpect(jsonPath("$[0].nome").value("João"));
        }

        @Test
        void deveBuscarAdotantePorId() throws Exception {

                AdotanteResponseDTO response = new AdotanteResponseDTO();
                response.setId(1L);
                response.setNome("João");

                Mockito.when(service.buscarPorId(1L))
                                .thenReturn(response);

                mockMvc.perform(get("/adotantes/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.nome").value("João"));
        }

        @Test
        void deveAtualizarAdotante() throws Exception {

                AdotanteRequestDTO request = new AdotanteRequestDTO();
                request.setNome("Maria");

                AdotanteResponseDTO response = new AdotanteResponseDTO();
                response.setId(1L);
                response.setNome("Maria");

                Mockito.when(service.atualizar(eq(1L), any()))
                                .thenReturn(response);

                mockMvc.perform(put("/adotantes/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.nome").value("Maria"));
        }

        @Test
        void deveDeletarAdotante() throws Exception {

                Mockito.doNothing()
                                .when(service)
                                .deletar(1L);

                mockMvc.perform(delete("/adotantes/1"))
                                .andExpect(status().isNoContent());
        }
}