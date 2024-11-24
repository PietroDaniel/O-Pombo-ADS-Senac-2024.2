package com.pombo.pombo.model.seletor;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public static void aplicarFiltroPeriodo(Root root,
                                            CriteriaBuilder cb, List<Predicate> predicates,
                                            LocalDate startDate, LocalDate endDate, String attributeName) {
        if (startDate != null && endDate != null) {
        	
        	LocalDateTime inicioDoDia = startDate.atStartOfDay();
            LocalDateTime finalDoDia = endDate.atTime(23, 59, 59, 999999999);
            predicates.add(cb.between(root.get(attributeName), inicioDoDia, finalDoDia));
        } else if (startDate != null) {
            // Apenas dataInicial fornecida
            LocalDateTime inicioDoDia = startDate.atStartOfDay();
            predicates.add(cb.greaterThanOrEqualTo(root.get(attributeName), inicioDoDia));
        } else if (endDate != null) {
            // Apenas dataFinal fornecida
            LocalDateTime finalDoDia = endDate.atTime(23, 59, 59, 999999999);
            predicates.add(cb.lessThanOrEqualTo(root.get(attributeName), finalDoDia));
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
