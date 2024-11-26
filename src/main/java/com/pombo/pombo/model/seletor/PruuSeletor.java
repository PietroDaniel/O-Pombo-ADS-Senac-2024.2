package com.pombo.pombo.model.seletor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private LocalDate dataInicioCriacao;
    private LocalDate dataFimCriacao;
    private Integer usuarioId;
    private String usuarioNome;
    private String excluido;
    private String bloqueado;
    private boolean curtidosPeloUsuario; 
    private Long curtidoPorUsuarioId; 


    @JsonProperty("estaCurtido")
    private boolean estaCurtido;

    @Override
    public Predicate toPredicate(Root<Pruu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        java.util.List<Predicate> predicates = new ArrayList<>();

        if (this.isCurtidosPeloUsuario() && this.getCurtidoPorUsuarioId() != null) {
            // Filtro que verifica se o Pruu foi curtido pelo usuário logado
            predicates.add(cb.isMember(
                this.getCurtidoPorUsuarioId(),
                root.get("likedByUsers").get("id")
            ));
        }
        
                
        if (this.getTexto() != null && !this.getTexto().trim().isEmpty()) {
            // WHERE/AND  COLUMN  OPERATOR         VALUE
            //   where    texto     like    '%substring do texto%'
            predicates.add(cb.like(root.get("texto"), "%" + this.getTexto() + "%"));
        }

        if(this.getUsuarioId() != null) {
            predicates.add(cb.equal(root.get("usuario").get("id"), this.getUsuarioId()));
        }

        if (this.getUsuarioNome() != null && !this.getUsuarioNome().trim().isEmpty()) {

            predicates.add(cb.like(root.get("usuario").get("nome"), "%" + this.getUsuarioNome() + "%"));
        }

        if(this.getExcluido()!= null) {

            boolean excluido = false;
            if (this.getBloqueado().equalsIgnoreCase("true")){
                excluido=true;
            }

            predicates.add(cb.equal(root.get("excluido"), excluido));
        }

        if(this.getBloqueado() != null) {

            boolean bloqueado = false;
            if (this.getBloqueado().equalsIgnoreCase("true")){
              bloqueado=true;
            }
            predicates.add(cb.equal(root.get("bloqueado"), bloqueado));

        }

        aplicarFiltroPeriodo(root, cb, predicates, this.getDataInicioCriacao(),
                this.getDataFimCriacao(), "dataHoraCriacao");

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
