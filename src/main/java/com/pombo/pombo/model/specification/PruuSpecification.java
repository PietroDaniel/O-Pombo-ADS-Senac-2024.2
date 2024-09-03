package com.pombo.pombo.model.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.seletor.PruuSeletor;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class PruuSpecification {

    public static Specification<Pruu> comFiltros(PruuSeletor seletor) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (seletor.getTexto() != null && seletor.getTexto().trim().length() > 0) {
                predicates.add(cb.like(root.get("texto"), "%" + seletor.getTexto() + "%"));
            }

            if (seletor.getDataInicioCriacao() != null && seletor.getDataFimCriacao() != null) {
                predicates.add(cb.between(root.get("dataHoraCriacao"), seletor.getDataInicioCriacao(), seletor.getDataFimCriacao()));
            } else if (seletor.getDataInicioCriacao() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dataHoraCriacao"), seletor.getDataInicioCriacao()));
            } else if (seletor.getDataFimCriacao() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dataHoraCriacao"), seletor.getDataFimCriacao()));
            }

            if (seletor.getQuantidadeMinimaLikes() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("quantidadeLikes"), seletor.getQuantidadeMinimaLikes()));
            }

            if (seletor.getBloqueado() != null) {
                predicates.add(cb.equal(root.get("bloqueado"), seletor.getBloqueado()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
