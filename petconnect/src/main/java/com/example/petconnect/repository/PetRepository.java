package com.example.petconnect.repository;

import com.example.petconnect.entity.Pet;
import com.example.petconnect.entity.enums.StatusPet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
    long countByStatus(StatusPet status);
}