package com.example.petconnect.controller;

import com.example.petconnect.dto.adocao.*;
import com.example.petconnect.service.AdocaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adocoes")
public class AdocaoController {

    private final AdocaoService adocaoService;

    public AdocaoController(AdocaoService adocaoService) {
        this.adocaoService = adocaoService;
    }

    @PostMapping
    public ResponseEntity<AdocaoResponseDTO> criar(@RequestBody @Valid AdocaoRequestDTO dto) {
        AdocaoResponseDTO response = adocaoService.criarAdocao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AdocaoResponseDTO>> listar() {
        return ResponseEntity.ok(adocaoService.listar());
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<AdocaoResponseDTO> aprovar(@PathVariable Long id) {
        AdocaoResponseDTO response = adocaoService.aprovarAdocao(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<AdocaoResponseDTO> cancelar(@PathVariable Long id) {
        AdocaoResponseDTO response = adocaoService.cancelarAdocao(id);
        return ResponseEntity.ok(response);
    }
}