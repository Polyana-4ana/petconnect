package com.example.petconnect.repository;

import com.example.petconnect.entity.Pet;
import com.example.petconnect.entity.enums.StatusPet;
import org.springframework.data.jpa.domain.Specification;

public class PetSpecification {

    public static Specification<Pet> nomeContains(String nome) {
        return (root, query, cb) ->
                nome == null ? null :
                        cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }

    public static Specification<Pet> especieEquals(String especie) {
        return (root, query, cb) ->
                especie == null ? null :
                        cb.equal(cb.lower(root.get("especie")), especie.toLowerCase());
    }

    public static Specification<Pet> idadeGreaterThanOrEq(Integer idadeMin) {
        return (root, query, cb) ->
                idadeMin == null ? null :
                        cb.greaterThanOrEqualTo(root.get("idade"), idadeMin);
    }

    public static Specification<Pet> idadeLessThanOrEq(Integer idadeMax) {
        return (root, query, cb) ->
                idadeMax == null ? null :
                        cb.lessThanOrEqualTo(root.get("idade"), idadeMax);
    }

    public static Specification<Pet> statusEquals(StatusPet status) {
        return (root, query, cb) ->
                status == null ? null :
                        cb.equal(root.get("status"), status);
    }
}