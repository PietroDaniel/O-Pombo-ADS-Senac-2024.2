package com.pombo.pombo.model.seletor;

import lombok.Data;

@Data
public class UsuarioSeletor extends BaseSeletor {

    private String nome;
    private String email;
    private String cpf;

    /**
     * Verifica se este seletor tem algum campo preenchido
     * @return true caso ao menos um dos atributos tenha sido preenchido
     */
    public boolean temFiltro() {
        return (this.nome != null && this.nome.trim().length() > 0)
            || (this.email != null && this.email.trim().length() > 0)
            || (this.cpf != null && this.cpf.trim().length() > 0);
    }
}
