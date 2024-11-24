package com.pombo.pombo.controller;

import java.io.IOException;
import java.util.List;

import com.pombo.pombo.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.model.seletor.UsuarioSeletor;
import com.pombo.pombo.service.UsuarioService;

import jakarta.servlet.annotation.MultipartConfig;

import com.pombo.pombo.exception.PomboException;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/usuarios")
@MultipartConfig(fileSizeThreshold = 10485760) // 10MB
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/salvar-foto-perfil")
    public void salvarFotoPerfil(@RequestParam("foto")MultipartFile foto) throws IOException, PomboException {

        Usuario subject = authenticationService.getAuthenticatedUser();

        if (foto == null) {
            throw new PomboException("Arquivo inválido!");
        }

        usuarioService.salvarFotoPerfil(foto, subject.getId());

    }

    @GetMapping("/todos")
    public List<Usuario> listarTodos() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) throws PomboException {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/novo")
    public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario novoUsuario) throws PomboException {
        Usuario usuarioCriado = usuarioService.criarUsuario(novoUsuario);
        return ResponseEntity.status(201).body(usuarioCriado);
    }


    @PutMapping
    public ResponseEntity<Usuario> atualizarUsuario(@RequestBody Usuario usuarioAtualizado)
            throws PomboException {

        Usuario subject = authenticationService.getAuthenticatedUser();

        usuarioAtualizado.setId(subject.getId());

        Usuario usuario = usuarioService.atualizarUsuario(usuarioAtualizado);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) throws PomboException {
        usuarioService.excluirUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/filtros")
    public List<Usuario> listarComFiltros(@RequestBody UsuarioSeletor seletor) {
        return usuarioService.listarComFiltros(seletor);
    }
}
