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
import com.pombo.pombo.exception.PomboException;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/usuarios")
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

    // @PostMapping("/{usuarioId}/like/{pruuUuid}")
    // public ResponseEntity<Void> likePruu(@PathVariable Long usuarioId, @PathVariable String pruuUuid)
    //         throws PomboException {
    //     usuarioService.likePruu(usuarioId, pruuUuid);
    //     return ResponseEntity.noContent().build();
    // }

    // @PostMapping("/{usuarioId}/unlike/{pruuUuid}")
    // public ResponseEntity<Void> unlikePruu(@PathVariable Long usuarioId, @PathVariable String pruuUuid)
    //         throws PomboException {
    //     usuarioService.unlikePruu(usuarioId, pruuUuid);
    //     return ResponseEntity.noContent().build();
    // }

    @PostMapping("/filtros")
    public List<Usuario> listarComFiltros(@RequestBody UsuarioSeletor seletor) {
        return usuarioService.listarComFiltros(seletor);
    }
}
