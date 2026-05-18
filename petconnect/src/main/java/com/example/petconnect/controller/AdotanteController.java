package com.example.petconnect.controller;

import com.example.petconnect.entity.Adotante;
import com.example.petconnect.service.AdotanteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adotantes")
public class AdotanteController {

    private final AdotanteService service;

    public AdotanteController(AdotanteService service) {
        this.service = service;
    }

    @PostMapping
    public Adotante cadastrar(@RequestBody Adotante adotante) {
        return service.cadastrar(adotante);
    }

    @GetMapping
    public List<Adotante> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Adotante buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Adotante atualizar(
            @PathVariable Long id,
            @RequestBody Adotante adotante) {
        return service.atualizar(id, adotante);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}