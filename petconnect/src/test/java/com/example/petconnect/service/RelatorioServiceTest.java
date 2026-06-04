package com.example.petconnect.service;

import com.example.petconnect.dto.AnaliseFunilDTO;
import com.example.petconnect.dto.RelatorioGeralDTO;
import com.example.petconnect.dto.RelatorioVendasDTO;
import com.example.petconnect.entity.enums.StatusPet;
import com.example.petconnect.repository.AdotanteRepository;
import com.example.petconnect.repository.PetRepository;
import com.example.petconnect.repository.RelatorioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RelatorioServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private AdotanteRepository adotanteRepository;

    @Mock
    private RelatorioRepository relatorioRepository;

    @InjectMocks
    private RelatorioService relatorioService;

    @BeforeEach
    void setUp() {
        // Setup padrão para os testes
    }

    @Test
    void testGerarRelatorio() {
        // Arrange
        when(petRepository.count()).thenReturn(10L);
        when(petRepository.countByStatus(StatusPet.DISPONIVEL)).thenReturn(5L);
        when(petRepository.countByStatus(StatusPet.RESERVADO)).thenReturn(2L);
        when(petRepository.countByStatus(StatusPet.ADOTADO)).thenReturn(3L);
        when(adotanteRepository.count()).thenReturn(8L);

        // Act
        RelatorioGeralDTO relatorio = relatorioService.gerarRelatorio();

        // Assert
        assertNotNull(relatorio);
        assertEquals(10L, relatorio.totalPets());
        assertEquals(5L, relatorio.disponiveis());
        assertEquals(2L, relatorio.reservados());
        assertEquals(3L, relatorio.adotados());
        assertEquals(8L, relatorio.totalAdotantes());
    }

    @Test
    void testAnalisarFunil_ComDatasValidas() {
        // Arrange
        LocalDate dataInicio = LocalDate.of(2026, 1, 1);
        LocalDate dataFim = LocalDate.of(2026, 6, 3);

        List<AnaliseFunilDTO> dadosMock = List.of(
                new AnaliseFunilDTO("PENDENTE", 50L, 0.0),
                new AnaliseFunilDTO("APROVADA", 30L, 0.0),
                new AnaliseFunilDTO("CANCELADA", 20L, 0.0)
        );

        when(relatorioRepository.agruparPorEstagioEntreData(dataInicio, dataFim))
                .thenReturn(dadosMock);

        // Act
        List<AnaliseFunilDTO> resultado = relatorioService.analisarFunil(dataInicio, dataFim);

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        
        // Validar cálculo de percentual
        assertEquals("PENDENTE", resultado.get(0).estagio());
        assertEquals(50L, resultado.get(0).quantidade());
        assertEquals(50.0, resultado.get(0).percentual()); // 50 / 100 * 100
        
        assertEquals("APROVADA", resultado.get(1).estagio());
        assertEquals(30.0, resultado.get(1).percentual()); // 30 / 100 * 100
        
        assertEquals("CANCELADA", resultado.get(2).estagio());
        assertEquals(20.0, resultado.get(2).percentual()); // 20 / 100 * 100
    }

    @Test
    void testAnalisarFunil_DataInicioNula() {
        // Act & Assert
        LocalDate dataFim = LocalDate.of(2026, 6, 3);
        assertThrows(IllegalArgumentException.class, 
                () -> relatorioService.analisarFunil(null, dataFim));
    }

    @Test
    void testAnalisarFunil_DataFimNula() {
        // Act & Assert
        LocalDate dataInicio = LocalDate.of(2026, 1, 1);
        assertThrows(IllegalArgumentException.class, 
                () -> relatorioService.analisarFunil(dataInicio, null));
    }

    @Test
    void testAnalisarFunil_DataInicioAposDataFim() {
        // Arrange
        LocalDate dataInicio = LocalDate.of(2026, 6, 3);
        LocalDate dataFim = LocalDate.of(2026, 1, 1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
                () -> relatorioService.analisarFunil(dataInicio, dataFim));
    }

    @Test
    void testAnalisarFunil_SemResultados() {
        // Arrange
        LocalDate dataInicio = LocalDate.of(2025, 1, 1);
        LocalDate dataFim = LocalDate.of(2025, 12, 31);

        when(relatorioRepository.agruparPorEstagioEntreData(dataInicio, dataFim))
                .thenReturn(List.of());

        // Act
        List<AnaliseFunilDTO> resultado = relatorioService.analisarFunil(dataInicio, dataFim);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testGerarRelatorioVendas_ComDatasValidas() {
        // Arrange
        LocalDate dataInicio = LocalDate.of(2026, 1, 1);

        List<RelatorioVendasDTO> dadosMock = List.of(
                new RelatorioVendasDTO(LocalDate.of(2026, 1, 1), 10L, BigDecimal.valueOf(1000.00), BigDecimal.ZERO),
                new RelatorioVendasDTO(LocalDate.of(2026, 2, 1), 15L, BigDecimal.valueOf(1500.00), BigDecimal.ZERO),
                new RelatorioVendasDTO(LocalDate.of(2026, 3, 1), 8L, BigDecimal.valueOf(800.00), BigDecimal.ZERO)
        );

        when(relatorioRepository.relatorioVendasPorMes(dataInicio))
                .thenReturn(dadosMock);

        // Act
        List<RelatorioVendasDTO> resultado = relatorioService.gerarRelatorioVendas(dataInicio);

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        
        // Validar cálculo de margem (usando compareTo para evitar problemas de escala do BigDecimal)
        assertEquals(0, resultado.get(0).margem().compareTo(BigDecimal.valueOf(100.00))); // 1000 * 0.10
        assertEquals(0, resultado.get(1).margem().compareTo(BigDecimal.valueOf(150.00))); // 1500 * 0.10
        assertEquals(0, resultado.get(2).margem().compareTo(BigDecimal.valueOf(80.00)));  // 800 * 0.10
    }

    @Test
    void testGerarRelatorioVendas_ValorTotalZero() {
        // Arrange
        LocalDate dataInicio = LocalDate.of(2026, 1, 1);

        List<RelatorioVendasDTO> dadosMock = List.of(
                new RelatorioVendasDTO(LocalDate.of(2026, 1, 1), 0L, BigDecimal.ZERO, BigDecimal.ZERO)
        );

        when(relatorioRepository.relatorioVendasPorMes(dataInicio))
                .thenReturn(dadosMock);

        // Act
        List<RelatorioVendasDTO> resultado = relatorioService.gerarRelatorioVendas(dataInicio);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(BigDecimal.ZERO, resultado.get(0).margem());
    }

    @Test
    void testGerarRelatorioVendas_SemResultados() {
        // Arrange
        LocalDate dataInicio = LocalDate.of(2024, 1, 1);

        when(relatorioRepository.relatorioVendasPorMes(dataInicio))
                .thenReturn(List.of());

        // Act
        List<RelatorioVendasDTO> resultado = relatorioService.gerarRelatorioVendas(dataInicio);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}
