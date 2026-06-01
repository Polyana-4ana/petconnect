package com.example.petconnect.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Home", description = "Endpoint de boas-vindas do sistema")
public class controllerExampleDoc {
    @GetMapping("/")
    @Operation(summary = "Verifica se a API está online", description = "Retorna uma mensagem simples de sucesso.")
    @ApiResponse(responseCode = "200", description = "API funcionando corretamente")
    public String home() {
        return "¡Opa! O projeto Petconnect está online e funcionando!";
    }
}
