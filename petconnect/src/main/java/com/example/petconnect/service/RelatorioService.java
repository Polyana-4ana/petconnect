package com.example.petconnect.service;

import com.example.petconnect.dto.AnaliseFunilDTO;
import com.example.petconnect.dto.RelatorioGeralDTO;
import com.example.petconnect.dto.RelatorioVendasDTO;
import com.example.petconnect.entity.enums.StatusPet; // <-- CORRIGIDO
import com.example.petconnect.repository.AdotanteRepository;
import com.example.petconnect.repository.PetRepository;
import com.example.petconnect.repository.RelatorioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RelatorioService {

    private final PetRepository petRepository;
    private final AdotanteRepository adotanteRepository;
    private final RelatorioRepository relatorioRepository;

    public RelatorioGeralDTO gerarRelatorio() {
        log.info("Gerando relatório geral");
        RelatorioGeralDTO relatorio = new RelatorioGeralDTO(
                petRepository.count(),
                petRepository.countByStatus(StatusPet.DISPONIVEL),
                petRepository.countByStatus(StatusPet.RESERVADO),
                petRepository.countByStatus(StatusPet.ADOTADO),
                adotanteRepository.count()
        );
        log.debug("Relatório geral gerado com sucesso");
        return relatorio;
    }

    public List<AnaliseFunilDTO> analisarFunil(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            log.warn("Datas nulas recebidas em analisarFunil");
            throw new IllegalArgumentException("Datas não podem ser nulas");
        }
        if (dataInicio.isAfter(dataFim)) {
            log.warn("Data início ({}) após data fim ({})", dataInicio, dataFim);
            throw new IllegalArgumentException("Data início não pode ser após data fim");
        }

        log.info("Analisando funil entre {} e {}", dataInicio, dataFim);
        List<AnaliseFunilDTO> resultados = relatorioRepository.agruparPorEstagioEntreData(dataInicio, dataFim);

        long total = resultados.stream()
                .mapToLong(a -> (long) a.quantidade()) // <-- CORRIGIDO: cast para long
                .sum();

        if (total == 0) {
            log.warn("Total de registros é zero no funil");
        }

        final long totalFinal = total;
        return resultados.stream()
                .map(r -> new AnaliseFunilDTO(
                        r.estagio(),
                        r.quantidade(),
                        totalFinal > 0 ? ((double) r.quantidade() / totalFinal) * 100.0 : 0.0
                ))
                .toList();
    }

    public List<RelatorioVendasDTO> gerarRelatorioVendas(LocalDate dataInicio) {
        log.info("Gerando relatório de vendas a partir de {}", dataInicio);
        return relatorioRepository.relatorioVendasPorMes(dataInicio)
                .stream()
                .map(r -> new RelatorioVendasDTO(
                        r.dataMes(),
                        r.quantidade(),
                        r.valorTotal(),
                        calcularMargem(r.valorTotal())
                ))
                .toList();
    }

    private BigDecimal calcularMargem(BigDecimal valorTotal) {
        if (valorTotal == null || valorTotal.signum() <= 0) {
            return BigDecimal.ZERO;
        }
        return valorTotal.multiply(new BigDecimal("0.10"));
    }
}
