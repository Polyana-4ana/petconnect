package com.example.petconnect.repository;

import com.example.petconnect.entity.Adotante;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AdotanteRepositoryTest {

    @Autowired
    private AdotanteRepository adotanteRepository;

    @Test
    @DisplayName("Salvar adotante e buscar pelo mesmo e-mail")
    void findByEmail_ComEmailExistente_DeveRetornarOptionalPresenteComNomeCorreto() {
        // Arrange
        Adotante adotante = Adotante.builder()
                .nome("João Silva")
                .email("joao@email.com")
                .telefone("11999999999")
                .cpf("123.456.789-00") // CPF obrigatório na sua Entity
                .build();
        adotanteRepository.save(adotante);

        // Act
        Optional<Adotante> resultadoOpt = adotanteRepository.findByEmail("joao@email.com");

        // Assert
        assertTrue(resultadoOpt.isPresent(), "O Optional deveria conter um adotante");
        Adotante adotanteRecuperado = resultadoOpt.get();
        assertEquals("João Silva", adotanteRecuperado.getNome());
        assertEquals("joao@email.com", adotanteRecuperado.getEmail());
        assertNotNull(adotanteRecuperado.getDataCadastro(), "A data de cadastro do @PrePersist deve ser preenchida");
    }

    @Test
    @DisplayName("Buscar por e-mail que não foi cadastrado")
    void findByEmail_ComEmailInexistente_DeveRetornarOptionalEmpty() {
        // Arrange
        String emailNaoCadastrado = "naoexiste@email.com";

        // Act
        Optional<Adotante> resultado = adotanteRepository.findByEmail(emailNaoCadastrado);

        // Assert
        assertTrue(resultado.isEmpty(), "O resultado esperado era um Optional.empty()");
    }

    @Test
    @DisplayName("saveAndFlush() — constraint unique")
    void saveAndFlush_ComEmailDuplicado_DeveLancarDataIntegrityViolationException() {
        // Arrange
        Adotante primeiroAdotante = Adotante.builder()
                .nome("Carlos")
                .email("duplicado@email.com")
                .telefone("11988888888")
                .cpf("111.111.111-11")
                .build();
        adotanteRepository.save(primeiroAdotante);

        Adotante segundoAdotante = Adotante.builder()
                .nome("Mariana")
                .email("duplicado@email.com")
                .telefone("11977777777")
                .cpf("222.222.222-22")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            adotanteRepository.saveAndFlush(segundoAdotante);
        }, "Deveria lançar DataIntegrityViolationException devido à restrição de e-mail único");
    }
}