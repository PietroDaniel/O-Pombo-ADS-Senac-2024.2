package com.pombo.pombo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.model.seletor.UsuarioSeletor;
import com.pombo.pombo.service.UsuarioService;
import com.pombo.pombo.exception.PomboException;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listarTodos() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) throws PomboException {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario novoUsuario) throws PomboException {
        Usuario usuarioCriado = usuarioService.criarUsuario(novoUsuario);
        return ResponseEntity.status(201).body(usuarioCriado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado)
            throws PomboException {
        Usuario usuario = usuarioService.atualizarUsuario(id, usuarioAtualizado);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) throws PomboException {
        usuarioService.excluirUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{usuarioId}/like/{pruuUuid}")
    public ResponseEntity<Void> likePruu(@PathVariable Long usuarioId, @PathVariable String pruuUuid)
            throws PomboException {
        usuarioService.likePruu(usuarioId, pruuUuid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{usuarioId}/unlike/{pruuUuid}")
    public ResponseEntity<Void> unlikePruu(@PathVariable Long usuarioId, @PathVariable String pruuUuid)
            throws PomboException {
        usuarioService.unlikePruu(usuarioId, pruuUuid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/filtros")
    public List<Usuario> listarComFiltros(@RequestBody UsuarioSeletor seletor) {
        return usuarioService.listarComFiltros(seletor);
    }
}
