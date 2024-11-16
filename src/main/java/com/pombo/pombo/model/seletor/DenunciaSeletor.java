package com.pombo.pombo.model.seletor;


import com.pombo.pombo.model.entity.Denuncia;
import com.pombo.pombo.model.enums.MotivoDenuncia;
import com.pombo.pombo.model.enums.SituacaoDenuncia;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class DenunciaSeletor extends BaseSeletor implements Specification<Denuncia> {

    private Long usuarioId;
    private String pruuId;
    private MotivoDenuncia motivoDenuncia;
    private SituacaoDenuncia situacaoDenunica;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFinal;


    @Override
    public Predicate toPredicate(Root<Denuncia> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<>();

        if (this.getUsuarioId() != null) {
            predicates.add(cb.equal(root.get("denunciante").get("id"), this.getUsuarioId()));
        }

        if (this.getPruuId() != null) {
            predicates.add(cb.equal(root.get("pruu").get("id"), this.getPruuId()));
        }

        if (this.getMotivoDenuncia() != null) {
            predicates.add(cb.equal(root.get("motivo"), this.getMotivoDenuncia()));
        }

        if (this.getSituacaoDenunica() != null) {
            predicates.add(cb.equal(root.get("situacao"), this.getSituacaoDenunica()));
        }

        aplicarFiltroPeriodo(root, cb, predicates, this.getDataInicio(),
                this.getDataFinal(), "dataCriacao");

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
