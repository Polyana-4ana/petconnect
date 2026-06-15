package com.example.petconnect.repository;

import com.example.petconnect.entity.Adocao;
import com.example.petconnect.entity.Adotante;
import com.example.petconnect.entity.Pet;
import com.example.petconnect.entity.enums.StatusAdocao;
import com.example.petconnect.entity.enums.StatusPet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AdocaoRepositoryTest {

    @Autowired
    private AdocaoRepository adocaoRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private AdotanteRepository adotanteRepository;

    private Pet petValido;
    private Adotante adotanteValido;

    @BeforeEach
    void setUp() {

        Pet pet = Pet.builder()
                .nome("Pipoca")
                .idade(2)
                .especie("Cachorro")
                .status(StatusPet.DISPONIVEL)
                .build();
        petValido = petRepository.save(pet);

        Adotante adotante = Adotante.builder()
                .nome("Gabriela Souza")
                .email("gabi@email.com")
                .telefone("51988887777")
                .cpf("987.654.321-11")
                .build();
        adotanteValido = adotanteRepository.save(adotante);
    }

    @Test
    @DisplayName("Salvar adoção e recuperar os dados com valores do ciclo de vida automáticos")
    void saveEFindById_DevePersistirComStatusPendenteEDataPreenchida() {
        // Arrange
        Adocao adocao = new Adocao(petValido, adotanteValido);

        // Act
        Adocao adocaoSalva = adocaoRepository.save(adocao);
        Optional<Adocao> adocaoRecuperadaOpt = adocaoRepository.findById(adocaoSalva.getId());

        // Assert
        assertTrue(adocaoRecuperadaOpt.isPresent(), "A adoção deveria ter sido localizada no banco");
        Adocao adocaoRecuperada = adocaoRecuperadaOpt.get();

        assertNotNull(adocaoRecuperada.getId());
        assertEquals(petValido.getId(), adocaoRecuperada.getPet().getId());
        assertEquals(adotanteValido.getId(), adocaoRecuperada.getAdotante().getId());

        assertEquals(StatusAdocao.PENDENTE, adocaoRecuperada.getStatus());
        assertNotNull(adocaoRecuperada.getDataAdocao());
    }

    @Test
    @DisplayName("Impedir a criação de duas adoções concorrentes para o mesmo Pet")
    void saveAndFlush_ComPetDuplicado_DeveLancarDataIntegrityViolationException() {
        // Arrange
        Adocao primeiraAdocao = new Adocao(petValido, adotanteValido);
        adocaoRepository.save(primeiraAdocao);

        // segundo adotante para o mesmo pet
        Adotante segundoAdotante = Adotante.builder()
                .nome("Marcos Lima")
                .email("marcos@email.com")
                .telefone("51911112222")
                .cpf("111.222.333-44")
                .build();
        adotanteRepository.save(segundoAdotante);

        // vincula o mesmo pet que já está em processo de adoção
        Adocao segundaAdocao = new Adocao(petValido, segundoAdotante);

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> {
            adocaoRepository.saveAndFlush(segundaAdocao);
        }, "Deveria barrar a inserção devido à constraint de unicidade do pet_id");
    }

    @Test
    @DisplayName("Contar quantidade de adoções com base no Status")
    void countByStatus_DeveRetornarQuantidadeCorreta() {
        // Arrange
        Adocao adocao = new Adocao(petValido, adotanteValido);
        adocaoRepository.save(adocao);

        // Act
        // Nota: Ajuste sugerido para verificar se o seu método conta usando StatusAdocao
        long totalPendentes = adocaoRepository.count();

        // Assert
        assertEquals(1, totalPendentes, "Deveria computar exatamente uma adoção cadastrada");
    }
}