package com.example.petconnect.repository;

import com.example.petconnect.entity.Adocao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdocaoRepository extends JpaRepository<Adocao, Long> {
}