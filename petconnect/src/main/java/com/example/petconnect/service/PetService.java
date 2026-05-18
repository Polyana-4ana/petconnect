package com.example.petconnect.service;

import com.example.petconnect.entity.Pet;

import java.util.List;

public interface PetService {

    Pet salvar(Pet pet);

    List<Pet> listar();

}