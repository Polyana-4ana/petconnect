package com.petconnect.service;

import com.petconnect.entity.Pet;

import java.util.List;

public interface PetService {

    Pet salvar(Pet pet);

    List<Pet> listar();

}