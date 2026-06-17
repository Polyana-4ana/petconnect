package com.example.petconnect.repository;

import com.example.petconnect.dto.AnaliseFunilDTO;
import com.example.petconnect.dto.RelatorioVendasDTO;
import com.example.petconnect.entity.Adocao;
import com.example.petconnect.entity.enums.StatusAdocao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RelatorioRepositoryImpl implements RelatorioRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<AnaliseFunilDTO> agruparPorEstagioEntreData(LocalDate dataInicio, LocalDate dataFim) {
        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.atTime(23, 59, 59, 999_999_999);

        String jpql = "SELECT CONCAT('', a.status), COUNT(a) FROM Adocao a " +
                "WHERE a.dataAdocao BETWEEN :inicio AND :fim GROUP BY a.status";

        TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
        query.setParameter("inicio", inicio);
        query.setParameter("fim", fim);

        List<Object[]> results = query.getResultList();
        List<AnaliseFunilDTO> lista = new ArrayList<>();

        for (Object[] row : results) {
            String estagio = (String) row[0];
            long quantidade = ((Number) row[1]).longValue();
            lista.add(new AnaliseFunilDTO(estagio, quantidade, 0.0));
        }

        return lista;
    }

    @Override
    public List<RelatorioVendasDTO> relatorioVendasPorMes(LocalDate dataInicio) {
        LocalDateTime inicio = dataInicio.atStartOfDay();

        String jpql = "SELECT FUNCTION('YEAR', a.dataAdocao), FUNCTION('MONTH', a.dataAdocao), COUNT(a) " +
                "FROM Adocao a " +
                "WHERE a.dataAdocao >= :inicio AND a.status = :status " +
                "GROUP BY FUNCTION('YEAR', a.dataAdocao), FUNCTION('MONTH', a.dataAdocao) " +
                "ORDER BY FUNCTION('YEAR', a.dataAdocao), FUNCTION('MONTH', a.dataAdocao)";

        TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
        query.setParameter("inicio", inicio);
        query.setParameter("status", StatusAdocao.APROVADA);

        List<Object[]> results = query.getResultList();
        List<RelatorioVendasDTO> lista = new ArrayList<>();

        for (Object[] row : results) {
            Integer year = ((Number) row[0]).intValue();
            Integer month = ((Number) row[1]).intValue();
            long quantidade = ((Number) row[2]).longValue();

            LocalDate dataMes = LocalDate.of(year, Month.of(month), 1);
            lista.add(new RelatorioVendasDTO(dataMes, quantidade, BigDecimal.ZERO, BigDecimal.ZERO));
        }

        return lista;
    }
}
