package com.example.petconnect.repository;

import com.example.petconnect.entity.Adocao;
import com.example.petconnect.entity.enums.StatusPet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdocaoRepository extends JpaRepository<Adocao, Long> {
    long countByStatus(StatusPet status);

}