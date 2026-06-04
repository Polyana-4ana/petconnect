package com.example.petconnect.repository;

import com.example.petconnect.dto.AnaliseFunilDTO;
import com.example.petconnect.dto.RelatorioVendasDTO;

import java.time.LocalDate;
import java.util.List;

public interface RelatorioRepository {
    List<AnaliseFunilDTO> agruparPorEstagioEntreData(LocalDate dataInicio, LocalDate dataFim);

    List<RelatorioVendasDTO> relatorioVendasPorMes(LocalDate dataInicio);
}
