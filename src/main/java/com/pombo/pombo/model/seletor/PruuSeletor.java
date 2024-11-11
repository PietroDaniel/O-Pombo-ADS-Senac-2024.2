package com.pombo.pombo.model.seletor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.entity.Usuario;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;

@Data
public class PruuSeletor extends BaseSeletor implements Specification<Pruu> {

    private String texto;
    private LocalDateTime dataInicioCriacao;
    private LocalDateTime dataFimCriacao;
    private Integer usuarioId;

    @JsonProperty("estaCurtido")
    private boolean estaCurtido;

    @Override
    public Predicate toPredicate(Root<Pruu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        java.util.List<Predicate> predicates = new ArrayList<>();

        if (this.getTexto() != null && !this.getTexto().trim().isEmpty()) {
            // WHERE/AND  COLUMN  OPERATOR         VALUE
            //   where    texto     like    '%substring do texto%'
            predicates.add(cb.like(root.get("texto"), "%" + this.getTexto() + "%"));
        }

        if(this.getUsuarioId() != null) {
            predicates.add(cb.equal(root.get("usuario"), this.getUsuarioId()));
        }

        aplicarFiltroPeriodo(root, cb, predicates, this.getDataInicioCriacao(), this.getDataFimCriacao(), "criadoEm");

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
