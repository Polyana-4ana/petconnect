package com.example.petconnect.controller;

import com.example.petconnect.entity.Pet;
import com.example.petconnect.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService service;

    @PostMapping
    public Pet salvar(@RequestBody Pet pet) {
        return service.salvar(pet);
    }

    @GetMapping
    public List<Pet> listar() {
        return service.listar();
    }
}