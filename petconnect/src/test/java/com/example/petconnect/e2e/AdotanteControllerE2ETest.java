package com.example.petconnect.e2e;

import com.example.petconnect.repository.AdotanteRepository;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AdotanteControllerE2ETest {

    @Autowired private MockMvc mockMvc;
    @Autowired private AdotanteRepository adotanteRepository;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        adotanteRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve cadastrar um adotante com sucesso e gerar ID e Data de Cadastro")
    void deveCadastrarAdotanteComSucesso() throws Exception {
        String jsonRequest = """
        {
            "nome": "Rodrigo Alves",
            "email": "rodrigo@email.com",
            "telefone": "11999998888",
            "cpf": "123.456.789-10"
        }
        """;

        mockMvc.perform(post("/adotantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Rodrigo Alves"))
                .andExpect(jsonPath("$.email").value("rodrigo@email.com"))
                .andExpect(jsonPath("$.telefone").value("11999998888"));
    }

    @Test
    @DisplayName("Deve buscar um adotante cadastrado pelo ID")
    void deveBuscarAdotantePorIdComSucesso() throws Exception {
        String jsonRequest = """
        {
            "nome": "Marisa Cruz",
            "email": "marisa@email.com",
            "telefone": "11977776666",
            "cpf": "123.456.789-20"
        }
        """;

        MvcResult result = mockMvc.perform(post("/adotantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andReturn();

        JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
        long idGerado = node.get("id").asLong();

        mockMvc.perform(get("/adotantes/" + idGerado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idGerado))
                .andExpect(jsonPath("$.nome").value("Marisa Cruz"));
    }

    @Test
    @DisplayName("Deve listar todos os adotantes cadastrados no banco")
    void deveListarAdotantesComSucesso() throws Exception {

        String adotante1 = "{\"nome\":\"Alice\",\"email\":\"alice@email.com\",\"telefone\":\"11911112222\",\"cpf\":\"123.456.789-30\"}";
        String adotante2 = "{\"nome\":\"Bob\",\"email\":\"bob@email.com\",\"telefone\":\"11922223333\",\"cpf\":\"123.456.789-40\"}";

        mockMvc.perform(post("/adotantes").contentType(MediaType.APPLICATION_JSON).content(adotante1)).andExpect(status().isCreated());
        mockMvc.perform(post("/adotantes").contentType(MediaType.APPLICATION_JSON).content(adotante2)).andExpect(status().isCreated());

        mockMvc.perform(get("/adotantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome").value("Alice"))
                .andExpect(jsonPath("$[1].nome").value("Bob"));
    }

    @Test
    @DisplayName("Deve atualizar os dados de um adotante existente")
    void deveAtualizarAdotanteComSucesso() throws Exception {
        String cadastroJson = "{\"nome\":\"Lucas\",\"email\":\"lucas@email.com\",\"telefone\":\"11933334444\",\"cpf\":\"123.456.789-50\"}";
        MvcResult result = mockMvc.perform(post("/adotantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cadastroJson))
                .andReturn();

        JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
        long idGerado = node.get("id").asLong();

        String atualizacaoJson = "{\"nome\":\"Lucas Silva\",\"email\":\"lucas.novo@email.com\",\"telefone\":\"11955554444\",\"cpf\":\"123.456.789-50\"}";

        mockMvc.perform(put("/adotantes/" + idGerado)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(atualizacaoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idGerado))
                .andExpect(jsonPath("$.nome").value("Lucas Silva"))
                .andExpect(jsonPath("$.email").value("lucas.novo@email.com"));
    }

    @Test
    @DisplayName("Deve deletar um adotante e retornar 204 No Content")
    void deveDeletarAdotanteComSucesso() throws Exception {
        String cadastroJson = "{\"nome\":\"Diego\",\"email\":\"diego@email.com\",\"telefone\":\"11944445555\",\"cpf\":\"123.456.789-60\"}";
        MvcResult result = mockMvc.perform(post("/adotantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cadastroJson))
                .andReturn();

        JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
        long idGerado = node.get("id").asLong();

        mockMvc.perform(delete("/adotantes/" + idGerado))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/adotantes/" + idGerado))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request se falhar nas anotações do DTO (nome em branco)")
    void deveRetornar400QuandoNomeForInvalido() throws Exception {
        String jsonInvalido = """
        {
            "nome": "",
            "email": "erro@email.com",
            "telefone": "11988887777",
            "cpf": "123.456.789-70"
        }
        """;

        mockMvc.perform(post("/adotantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }
}