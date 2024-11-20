package com.pombo.pombo.java.factories;

import com.pombo.pombo.java.utils.GeradorDeCPFs;
import com.pombo.pombo.model.entity.Usuario;

import java.util.UUID;

public class UsuarioFactory {

    public static Usuario criarUsuario() {

        Usuario usuario = new Usuario();

        usuario.setNome("Nome");
        usuario.setEmail(UUID.randomUUID() + "@email.com");
        usuario.setCpf(GeradorDeCPFs.generateValidCPF());
        usuario.setPassword("Senha123");

        return usuario;
    }

}
