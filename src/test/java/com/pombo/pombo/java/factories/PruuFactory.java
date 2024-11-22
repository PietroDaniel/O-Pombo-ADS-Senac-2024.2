package com.pombo.pombo.java.factories;

import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.entity.Usuario;

import java.util.ArrayList;
import java.util.UUID;

public class PruuFactory {

    public static Pruu criarPruu(Usuario usuario) {
        Pruu pruu = new Pruu();

        pruu.setId(UUID.randomUUID().toString());
        pruu.setUsuario(usuario);
        pruu.setTexto("Texto do pruu padrão para testes");
        pruu.setFoto(null); // Nenhuma foto padrão
        pruu.setLikedByUsers(new ArrayList<>());
        pruu.setDenuncias(new ArrayList<>());
        pruu.setBloqueado(false);
        pruu.setExcluido(false);

        return pruu;
    }
}
