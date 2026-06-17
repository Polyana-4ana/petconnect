package com.example.petconnect.service;

import com.example.petconnect.dto.AnaliseFunilDTO;
import com.example.petconnect.dto.RelatorioGeralDTO;
import com.example.petconnect.dto.RelatorioVendasDTO;
import com.example.petconnect.entity.enums.StatusPet;
import com.example.petconnect.repository.AdotanteRepository;
import com.example.petconnect.repository.PetRepository;
import com.example.petconnect.repository.RelatorioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void deveGerarRelatorioGeral() {
        when(petRepository.count()).thenReturn(10L);
        when(petRepository.countByStatus(StatusPet.DISPONIVEL)).thenReturn(5L);
        when(petRepository.countByStatus(StatusPet.RESERVADO)).thenReturn(3L);
        when(petRepository.countByStatus(StatusPet.ADOTADO)).thenReturn(2L);
        when(adotanteRepository.count()).thenReturn(4L);

        RelatorioGeralDTO dto = relatorioService.gerarRelatorio();

        assertNotNull(dto);
        assertEquals(10L, dto.totalPets());
        assertEquals(5L, dto.disponiveis());
        assertEquals(3L, dto.reservados());
        assertEquals(2L, dto.adotados());
        assertEquals(4L, dto.totalAdotantes());
    }

    @Test
    void deveAnalisarFunilComDadosValidos() {
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 1, 31);

        when(relatorioRepository.agruparPorEstagioEntreData(inicio, fim))
                .thenReturn(List.of(new AnaliseFunilDTO("PENDENTE", 2, 0.0)));

        List<AnaliseFunilDTO> resultado = relatorioService.analisarFunil(inicio, fim);

        assertEquals(1, resultado.size());
        assertEquals("PENDENTE", resultado.get(0).estagio());
    }

    @Test
    void deveGerarRelatorioVendasComDadosValidos() {
        LocalDate dataInicio = LocalDate.of(2026, 1, 1);

        when(relatorioRepository.relatorioVendasPorMes(dataInicio))
                .thenReturn(List.of(new RelatorioVendasDTO(dataInicio, 5L, BigDecimal.ZERO, BigDecimal.ZERO)));

        List<RelatorioVendasDTO> resultado = relatorioService.gerarRelatorioVendas(dataInicio);

        assertEquals(1, resultado.size());
        assertEquals(5L, resultado.get(0).quantidade());
    }
}
