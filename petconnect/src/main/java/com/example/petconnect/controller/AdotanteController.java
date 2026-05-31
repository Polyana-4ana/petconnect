package com.example.petconnect.controller;

import com.example.petconnect.dto.adotante.*;
import com.example.petconnect.entity.Adotante;
import com.example.petconnect.service.AdotanteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AdotanteResponseDTO> cadastrar(@RequestBody @Valid AdotanteRequestDTO dto) {
        AdotanteResponseDTO response = service.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AdotanteResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdotanteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdotanteResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid AdotanteRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}