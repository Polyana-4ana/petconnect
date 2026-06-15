package com.example.petconnect.repository;

import com.example.petconnect.entity.Pet;
import com.example.petconnect.entity.enums.StatusPet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PetRepositoryTest {

    @Autowired
    private PetRepository petRepository;

    @Test
    @DisplayName("Salvar pet e recuperar pelo ID gerado")
    void saveEFindById_DevePersistirERecuperarComSucesso() {
        // Arrange
        Pet pet = Pet.builder()
                .nome("Thor")
                .idade(3)
                .especie("Cachorro")
                .status(StatusPet.DISPONIVEL)
                .build();

        // Act
        Pet petSalvo = petRepository.save(pet);
        Optional<Pet> petRecuperadoOpt = petRepository.findById(petSalvo.getId());

        // Assert
        assertTrue(petRecuperadoOpt.isPresent(), "O pet deveria ser encontrado no banco");
        Pet petRecuperado = petRecuperadoOpt.get();

        assertNotNull(petRecuperado.getId(), "O ID gerado não deve ser nulo");
        assertEquals("Thor", petRecuperado.getNome());
        assertEquals(3, petRecuperado.getIdade());
        assertEquals("Cachorro", petRecuperado.getEspecie());
        assertEquals(StatusPet.DISPONIVEL, petRecuperado.getStatus());
    }

    @Test
    @DisplayName("Buscar por ID que não existe no banco")
    void findById_ComIdInexistente_DeveRetornarOptionalEmpty() {
        // Arrange
        Long idInexistente = 999L;

        // Act
        Optional<Pet> resultado = petRepository.findById(idInexistente);

        // Assert
        assertTrue(resultado.isEmpty(), "O resultado esperado era Optional.empty()");
    }

    @Test
    @DisplayName("Atualizar dados de um Pet existente")
    void update_DeveAlterarDadosPreservandoOID() {
        // Arrange
        Pet pet = Pet.builder().nome("Bolinha").idade(1).especie("Gato").status(StatusPet.DISPONIVEL).build();
        Pet petSalvo = petRepository.save(pet);
        Long idOriginal = petSalvo.getId();

        // Act - Modifica os dados e salva novamente
        petSalvo.setNome("Bolinha Atualizado");
        petSalvo.setStatus(StatusPet.ADOTADO);
        Pet petAtualizado = petRepository.save(petSalvo);

        // Assert
        assertEquals(idOriginal, petAtualizado.getId(), "O ID deve permanecer o mesmo");
        assertEquals("Bolinha Atualizado", petAtualizado.getNome());
        assertEquals(StatusPet.ADOTADO, petAtualizado.getStatus());
    }

    @Test
    @DisplayName("Deletar um Pet com sucesso")
    void delete_DeveRemoverOPetDoBanco() {
        // Arrange
        Pet pet = Pet.builder().nome("Rex").idade(5).especie("Cachorro").status(StatusPet.DISPONIVEL).build();
        Pet petSalvo = petRepository.save(pet);

        // Act
        petRepository.delete(petSalvo);
        Optional<Pet> resultado = petRepository.findById(petSalvo.getId());

        // Assert
        assertTrue(resultado.isEmpty(), "O pet não deveria mais existir no banco de dados");
    }

    @Test
    @DisplayName("Salvar pet com nome nulo deve falhar se a coluna for obrigatória")
    void save_ComNomeNulo_DeveLancarExcecao() {
        // Arrange
        Pet petInvalido = Pet.builder()
                .nome(null)
                .idade(2)
                .especie("Gato")
                .status(StatusPet.DISPONIVEL)
                .build();

        // Act & Assert
        assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
            petRepository.saveAndFlush(petInvalido);
        }, "Deveria lançar ConstraintViolationException devido à validação do Bean Validation no campo nome");
    }
}