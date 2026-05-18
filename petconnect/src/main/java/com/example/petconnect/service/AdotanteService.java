package com.petconnect.service;

import com.petconnect.entity.Adotante;
import com.petconnect.repository.AdotanteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdotanteService {

    private final AdotanteRepository repository;

    public AdotanteService(AdotanteRepository repository) {
        this.repository = repository;
    }

    public Adotante cadastrar(Adotante adotante) {

        if (adotante.getNome() == null || adotante.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        if (adotante.getEmail() == null || adotante.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }

        repository.findByEmail(adotante.getEmail())
                .ifPresent(a -> {
                    throw new IllegalArgumentException("Email já cadastrado");
                });

        return repository.save(adotante);
    }

    public List<Adotante> listar() {
        return repository.findAll();
    }

    public Adotante buscarPorId(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Adotante não encontrado"));
    }

    public Adotante atualizar(Long id, Adotante adotanteAtualizado) {

        Adotante adotante = buscarPorId(id);

        adotante.setNome(adotanteAtualizado.getNome());
        adotante.setEmail(adotanteAtualizado.getEmail());
        adotante.setTelefone(adotanteAtualizado.getTelefone());

        return repository.save(adotante);
    }

    public void deletar(Long id) {

        Adotante adotante = buscarPorId(id);

        repository.delete(adotante);
    }
}