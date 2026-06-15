package com.example.petconnect.e2e;

import com.example.petconnect.repository.AdocaoRepository;
import com.example.petconnect.repository.AdotanteRepository;
import com.example.petconnect.repository.PetRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AdocaoControllerE2ETest {

    @Autowired private MockMvc mockMvc;
    @Autowired private AdocaoRepository adocaoRepository;
    @Autowired private PetRepository petRepository;
    @Autowired private AdotanteRepository adotanteRepository;
    @Autowired private ObjectMapper objectMapper;

    private Long petIdSalvo;
    private Long adotanteIdSalvo;

    @BeforeEach
    void setup() throws Exception {

        adocaoRepository.deleteAll();
        petRepository.deleteAll();
        adotanteRepository.deleteAll();


        String petJson = """
        {
            "nome": "Rex",
            "idade": 3,
            "especie": "Cachorro"
        }
        """;
        MvcResult petResult = mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson))
                .andExpect(status().isCreated())
                .andReturn();
        JsonNode petNode = objectMapper.readTree(petResult.getResponse().getContentAsString());
        this.petIdSalvo = petNode.get("id").asLong();


        String adotanteJson = """
        {
            "nome": "Gabriela Souza",
            "email": "gabi.e2e@email.com",
            "telefone": "51988887777",
            "cpf": "987.654.321-11"
        }
        """;
        MvcResult adotanteResult = mockMvc.perform(post("/adotantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adotanteJson))
                .andExpect(status().isCreated())
                .andReturn();
        JsonNode adotanteNode = objectMapper.readTree(adotanteResult.getResponse().getContentAsString());
        this.adotanteIdSalvo = adotanteNode.get("id").asLong();
    }

    @Test
    @DisplayName("Deve criar uma adoção com sucesso utilizando IDs dinâmicas geradas no Setup")
    void deveCriarAdocaoComSucesso() throws Exception {

        String adocaoJson = String.format("{\"petId\": %d, \"adotanteId\": %d}", petIdSalvo, adotanteIdSalvo);

        mockMvc.perform(post("/adocoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adocaoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("PENDENTE"))
                .andExpect(jsonPath("$.nomePet").value("Rex"))
                .andExpect(jsonPath("$.nomeAdotante").value("Gabriela Souza"));
    }

    @Test
    @DisplayName("Deve aprovar uma adoção pendente com sucesso")
    void deveAprovarAdocaoComSucesso() throws Exception {
        String adocaoJson = String.format("{\"petId\": %d, \"adotanteId\": %d}", petIdSalvo, adotanteIdSalvo);

        mockMvc.perform(post("/adocoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(adocaoJson));

        Long idAdocao = adocaoRepository.findAll().get(0).getId();

        mockMvc.perform(put("/adocoes/" + idAdocao + "/aprovar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idAdocao))
                .andExpect(jsonPath("$.status").value("APROVADA"));
    }

    @Test
    @DisplayName("Deve cancelar uma adoção pendente com sucesso")
    void deveCancelarAdocaoComSucesso() throws Exception {
        String adocaoJson = String.format("{\"petId\": %d, \"adotanteId\": %d}", petIdSalvo, adotanteIdSalvo);

        mockMvc.perform(post("/adocoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(adocaoJson));

        Long idAd;

    }

}