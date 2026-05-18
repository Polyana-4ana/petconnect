package com.petconnect.controller;

import com.petconnect.entity.Adocao;
import com.petconnect.service.AdocaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adocoes")
public class AdocaoController {

    @Autowired
    private AdocaoService adocaoService;

    @PostMapping
    public Adocao criar(@RequestParam Long petId,
                        @RequestParam Long adotanteId) {
        return adocaoService.criarAdocao(petId, adotanteId);
    }

    @PutMapping("/{id}/aprovar")
    public Adocao aprovar(@PathVariable Long id) {
        return adocaoService.aprovarAdocao(id);
    }

    @PutMapping("/{id}/cancelar")
    public Adocao cancelar(@PathVariable Long id) {
        return adocaoService.cancelarAdocao(id);
    }
}