package com.pombo.pombo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pombo.pombo.exception.PomboException;
import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.model.repository.PruuRepository;
import com.pombo.pombo.model.repository.UsuarioRepository;
import com.pombo.pombo.model.seletor.UsuarioSeletor;

import jakarta.persistence.criteria.Predicate;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PruuRepository pruuRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) throws PomboException {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new PomboException("Usuário não encontrado"));
    }

    public Usuario criarUsuario(Usuario novoUsuario) {
        return usuarioRepository.save(novoUsuario);
    }

    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado) throws PomboException {
        Usuario usuario = buscarPorId(id);
        usuario.setNome(usuarioAtualizado.getNome());
        usuario.setEmail(usuarioAtualizado.getEmail());
        usuario.setCpf(usuarioAtualizado.getCpf());
        return usuarioRepository.save(usuario);
    }

    public void excluirUsuario(Long id) throws PomboException {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }

    public void likePruu(Long usuarioId, Long pruuId) throws PomboException {
        Usuario usuario = buscarPorId(usuarioId);
        Pruu pruu = pruuRepository.findById(pruuId)
            .orElseThrow(() -> new PomboException("Pruu não encontrado"));

        if (!usuario.getLikedPruus().contains(pruu)) {
            usuario.getLikedPruus().add(pruu);
            pruu.setQuantidadeLikes(pruu.getQuantidadeLikes() + 1);
            usuarioRepository.save(usuario);
            pruuRepository.save(pruu);
        }
    }

    public void unlikePruu(Long usuarioId, Long pruuId) throws PomboException {
        Usuario usuario = buscarPorId(usuarioId);
        Pruu pruu = pruuRepository.findById(pruuId)
            .orElseThrow(() -> new PomboException("Pruu não encontrado"));

        if (usuario.getLikedPruus().contains(pruu)) {
            usuario.getLikedPruus().remove(pruu);
            pruu.setQuantidadeLikes(pruu.getQuantidadeLikes() - 1);
            usuarioRepository.save(usuario);
            pruuRepository.save(pruu);
        }
    }

    public List<Usuario> listarComFiltros(UsuarioSeletor seletor) {
        return usuarioRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (seletor.getNome() != null && !seletor.getNome().isEmpty()) {
                predicates.add(cb.like(root.get("nome"), "%" + seletor.getNome() + "%"));
            }

            if (seletor.getEmail() != null && !seletor.getEmail().isEmpty()) {
                predicates.add(cb.like(root.get("email"), "%" + seletor.getEmail() + "%"));
            }

            if (seletor.getCpf() != null) {
                predicates.add(cb.equal(root.get("cpf"), seletor.getCpf()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

}
