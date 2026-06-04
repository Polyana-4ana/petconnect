package com.example.petconnect.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RelatorioVendasDTO(LocalDate dataMes, long quantidade, BigDecimal valorTotal, BigDecimal margem) {

}
