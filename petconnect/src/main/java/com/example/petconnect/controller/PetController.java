package com.example.petconnect.controller;

import com.example.petconnect.dto.pet.PetRequestDTO;
import com.example.petconnect.dto.pet.PetResponseDTO;
import com.example.petconnect.entity.enums.StatusPet;
import com.example.petconnect.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService service;

    @PostMapping
    public ResponseEntity<PetResponseDTO> salvar(
            @RequestBody @Valid PetRequestDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.salvar(dto));
    }

    @GetMapping
    public ResponseEntity<Page<PetResponseDTO>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String especie,
            @RequestParam(required = false) Integer idadeMin,
            @RequestParam(required = false) Integer idadeMax,
            @RequestParam(required = false) StatusPet status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                service.buscarComFiltro(
                        nome,
                        especie,
                        idadeMin,
                        idadeMax,
                        status,
                        pageable
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponseDTO> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid PetRequestDTO dto
    ) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id
    ) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}