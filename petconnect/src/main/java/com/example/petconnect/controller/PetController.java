package com.petconnect.controller;

import com.petconnect.entity.Pet;
import com.petconnect.service.PetService;
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