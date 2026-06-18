package com.example.petconnect.e2e;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.example.petconnect.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class PetControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }
        @Test
        void deveCadastrarPetComSucesso() throws Exception {

            String json = """
        {
            "nome":"Rex",
            "idade":3,
            "especie":"Cachorro"
        }
        """;

            mockMvc.perform(post("/pets")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.nome").value("Rex"))
                    .andExpect(jsonPath("$.status").value("DISPONIVEL"));
        }

    @Test
    void deveAtualizarPetComSucesso() throws Exception {

        String petOriginal = """
        {
            "nome":"Rex",
            "idade":3,
            "especie":"Cachorro"
        }
        """;

        mockMvc.perform(post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(petOriginal));

        Long id = repository.findAll().get(0).getId();

        String petAtualizado = """
        {
            "nome":"Thor",
            "idade":5,
            "especie":"Cachorro"
        }
        """;

        mockMvc.perform(put("/pets/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petAtualizado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Thor"))
                .andExpect(jsonPath("$.idade").value(5))
                .andExpect(jsonPath("$.especie").value("Cachorro"));
    }

     @Test
     void deveRetornar400QuandoNomeForNulo() throws Exception {

         String json = """
                {
                    "nome": null,
                    "idade":3,
                    "especie":"Cachorro"
                }
                """;

         mockMvc.perform(post("/pets")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(json))
                 .andExpect(status().isBadRequest());
     }

     @Test
     void deveListarPets() throws Exception {

         String pet1 = """
                {
                    "nome":"Rex",
                    "idade":3,
                    "especie":"Cachorro"
                }
                """;

         String pet2 = """
                {
                    "nome":"Luna",
                    "idade":2,
                    "especie":"Gato"
                }
                """;

         mockMvc.perform(post("/pets")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(pet1));

         mockMvc.perform(post("/pets")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(pet2));

         mockMvc.perform(get("/pets"))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.content").isArray())
                 .andExpect(jsonPath("$.content.length()").value(2));
     }

    @Test
    void deveDeletarPetComSucesso() throws Exception {

        String pet = """
        {
            "nome":"Rex",
            "idade":3,
            "especie":"Cachorro"
        }
        """;

        mockMvc.perform(post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pet));

        Long id = repository.findAll().get(0).getId();

        mockMvc.perform(delete("/pets/" + id))
                .andExpect(status().isNoContent());

        assertEquals(0, repository.count());
    }
 }

