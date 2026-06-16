package com.example.petconnect.repository;

import com.example.petconnect.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PetRepository extends JpaRepository<Pet, Long>,
        JpaSpecificationExecutor<Pet> {
}