package com.pombo.pombo.model.seletor;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false) // Adiciona esta anotação para evitar a chamada à superclasse
public class UsuarioSeletor extends BaseSeletor {

    private String nome;
    private String email;
    private String cpf;

    /**
     * Verifica se este seletor tem algum campo preenchido
     * @return true caso ao menos um dos atributos tenha sido preenchido
     */
    public boolean temFiltro() {
        return (this.nome != null && !this.nome.trim().isEmpty())
            || (this.email != null && !this.email.trim().isEmpty())
            || (this.cpf != null && !this.cpf.trim().isEmpty());
    }
}
