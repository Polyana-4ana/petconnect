package com.example.petconnect.repository;

import com.example.petconnect.entity.Adotante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdotanteRepository extends JpaRepository<Adotante, Long> {

    Optional<Adotante> findByEmail(String email);
}