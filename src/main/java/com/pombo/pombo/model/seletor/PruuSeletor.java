package com.pombo.pombo.model.seletor;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false) // Adiciona esta anotação para evitar a chamada à superclasse
public class PruuSeletor extends BaseSeletor {

    private String texto;
    private LocalDateTime dataInicioCriacao;
    private LocalDateTime dataFimCriacao;
    private Integer quantidadeMinimaLikes;
    private Boolean bloqueado;
    private String usuarioUuid;

    /**
     * Verifica se este seletor tem algum campo preenchido
     * 
     * @return true caso ao menos um dos atributos tenha sido preenchido
     */
    public boolean temFiltro() {
        return (this.texto != null && this.texto.trim().length() > 0)
                || (this.dataInicioCriacao != null)
                || (this.dataFimCriacao != null)
                || (this.quantidadeMinimaLikes != null && this.quantidadeMinimaLikes > 0)
                || (this.bloqueado != null)
                || (this.usuarioUuid != null && !this.usuarioUuid.isEmpty());
    }
}
