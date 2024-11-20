package com.pombo.pombo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pombo.pombo.exception.PomboException;
import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.model.repository.PruuRepository;
import com.pombo.pombo.model.repository.UsuarioRepository;
import com.pombo.pombo.model.seletor.UsuarioSeletor;

import jakarta.persistence.criteria.Predicate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ImagemService imagemService;

    public void salvarFotoPerfil(MultipartFile foto, Long usuarioId) throws PomboException {

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new PomboException("Usuário não encontrado!"));
        String imagemBase64 = imagemService.processarImagem(foto);
        usuario.setFoto(imagemBase64);
        usuarioRepository.save(usuario);
    }

    public Usuario criarUsuario(Usuario novoUsuario) throws PomboException {
        verificarSeUsuarioExiste(novoUsuario);
        return usuarioRepository.save(novoUsuario);
    }

    public Usuario atualizarUsuario(Usuario usuarioAtualizado) throws PomboException {
        verificarSeUsuarioExiste(usuarioAtualizado);
        return usuarioRepository.save(usuarioAtualizado);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) throws PomboException {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new PomboException("Usuário não encontrado"));
    }

    public List<Usuario> listarComFiltros(UsuarioSeletor seletor) {

        if (seletor.temPaginacao()) {
            int pageNumber = seletor.getPagina();
            int pageSize = seletor.getLimite();

            PageRequest page = PageRequest.of(pageNumber - 1, pageSize);
            return usuarioRepository.findAll(seletor, page).toList();
        }
        return usuarioRepository.findAll(seletor);
    }

    public void excluirUsuario(Long id) throws PomboException {
        Usuario usuario = buscarPorId(id);
        if (!usuario.getPruus().isEmpty()) {
            throw new PomboException("Usuário que possui mensagens não pode ser excluído");
        }
        usuarioRepository.delete(usuario);
    }

    public void verificarSeUsuarioExiste(Usuario usuario) throws PomboException {

        Optional<Usuario> usuarioComMesmoEmail = usuarioRepository.findByEmail(usuario.getEmail());
        Optional<Usuario> usuarioComMesmoCpf = usuarioRepository.findByCpf(usuario.getCpf());

        if (usuario.getId() == null) {
            usuario.setId(0L);
        }

        if (usuarioComMesmoEmail.isPresent()) {
            if (!usuario.getId().equals(usuarioComMesmoEmail.get().getId())) {
                throw new PomboException("Email ja cadastrado!");
            }
        }

        if (usuarioComMesmoCpf.isPresent()) {
            if (!usuario.getId().equals(usuarioComMesmoCpf.get().getId())) {
                throw new PomboException("Cpf ja cadastrado!");
            }
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado" + username));
    }
}
