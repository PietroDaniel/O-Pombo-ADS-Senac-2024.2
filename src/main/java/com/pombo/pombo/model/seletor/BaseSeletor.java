package com.pombo.pombo.model.seletor;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;

@Data
public abstract class BaseSeletor {

    private int pagina;
    private int limite;

    public BaseSeletor() {
        this.limite = 0;
        this.pagina = 0;
    }

    public boolean temPaginacao() {
        return this.limite > 0 && this.pagina > 0;
    }

    public int getOffset() {
        return this.limite * (this.pagina - 1);
    }

    public static void aplicarFiltroPeriodo(Root<?> root,
            CriteriaBuilder cb, List<Predicate> predicates,
            Integer valorMinimo, Integer valorMaximo, String nomeAtributo) {
        if (valorMinimo != null && valorMaximo != null) {
            // WHERE atributo BETWEEN min AND max
            predicates.add(cb.between(root.get(nomeAtributo), valorMinimo, valorMaximo));
        } else if (valorMinimo != null) {
            // WHERE atributo >= min
            predicates.add(cb.greaterThanOrEqualTo(root.get(nomeAtributo), valorMinimo));
        } else if (valorMaximo != null) {
            // WHERE atributo <= max
            predicates.add(cb.lessThanOrEqualTo(root.get(nomeAtributo), valorMaximo));
        }
    }

    public static void aplicarFiltroPeriodo(Root<?> root,
            CriteriaBuilder cb, List<Predicate> predicates,
            LocalDate dataInicial, LocalDate dataFinal, String nomeAtributo) {
        if (dataInicial != null && dataFinal != null) {
            // WHERE atributo BETWEEN min AND max
            predicates.add(cb.between(root.get(nomeAtributo), dataInicial, dataFinal));
        } else if (dataInicial != null) {
            // WHERE atributo >= min
            predicates.add(cb.greaterThanOrEqualTo(root.get(nomeAtributo), dataInicial));
        } else if (dataFinal != null) {
            // WHERE atributo <= max
            predicates.add(cb.lessThanOrEqualTo(root.get(nomeAtributo), dataFinal));
        }
    }

    public int getPagina() {
        return pagina;
    }

    public void setPagina(int pagina) {
        this.pagina = pagina;
    }

    public int getLimite() {
        return limite;
    }

    public void setLimite(int limite) {
        this.limite = limite;
    }
}
