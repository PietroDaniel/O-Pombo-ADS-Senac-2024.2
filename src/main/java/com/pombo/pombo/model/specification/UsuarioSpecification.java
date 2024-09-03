package com.pombo.pombo.model.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.model.seletor.UsuarioSeletor;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class UsuarioSpecification {

    public static Specification<Usuario> comFiltros(UsuarioSeletor seletor) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (seletor.getNome() != null && seletor.getNome().trim().length() > 0) {
                predicates.add(cb.like(root.get("nome"), "%" + seletor.getNome() + "%"));
            }

            if (seletor.getEmail() != null && seletor.getEmail().trim().length() > 0) {
                predicates.add(cb.like(root.get("email"), "%" + seletor.getEmail() + "%"));
            }

            if (seletor.getCpf() != null && seletor.getCpf().trim().length() > 0) {
                predicates.add(cb.equal(root.get("cpf"), seletor.getCpf()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
