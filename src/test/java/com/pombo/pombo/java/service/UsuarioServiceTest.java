package com.pombo.pombo.java.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.pombo.pombo.exception.PomboException;
import com.pombo.pombo.java.factories.UsuarioFactory;
import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.model.repository.UsuarioRepository;
import com.pombo.pombo.service.ImagemService;
import com.pombo.pombo.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ImagemService imagemService;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = UsuarioFactory.criarUsuario();
    }

    @Test
    @DisplayName("Deve criar um novo usuário com sucesso")
    public void testCriarUsuario_Sucesso() throws PomboException {
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario result = usuarioService.criarUsuario(usuario);

        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo(usuario.getNome());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("Deve atualizar um usuário com sucesso")
    public void testAtualizarUsuario_Sucesso() throws PomboException {
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario atualizado = usuarioService.atualizarUsuario(usuario);

        assertThat(atualizado).isNotNull();
        assertThat(atualizado.getNome()).isEqualTo(usuario.getNome());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("Deve retornar todos os usuários")
    public void testListarTodos_Sucesso() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(usuario);

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> result = usuarioService.listarTodos();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getEmail()).isEqualTo(usuario.getEmail());
    }

    @Test
    @DisplayName("Deve buscar um usuário por ID com sucesso")
    public void testBuscarPorId_Sucesso() throws PomboException {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.buscarPorId(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(usuario.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar um usuário inexistente por ID")
    public void testBuscarPorId_Falha() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.buscarPorId(999L))
                .isInstanceOf(PomboException.class)
                .hasMessageContaining("Usuário não encontrado");
    }

    @Test
    @DisplayName("Deve excluir um usuário com sucesso")
    public void testExcluirUsuario_Sucesso() throws PomboException {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findAll()).thenReturn(new ArrayList<>());

        usuarioService.excluirUsuario(1L);

        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir usuário com mensagens")
    public void testExcluirUsuario_ComMensagens() {
        Pruu pruu = new Pruu();
        pruu.setId("pruu-01");
        usuario.getPruus().add(pruu);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> usuarioService.excluirUsuario(1L))
                .isInstanceOf(PomboException.class)
                .hasMessageContaining("Usuário que possui mensagens não pode ser excluído");
    }

    @Test
    @DisplayName("Deve salvar uma foto de perfil com sucesso")
    public void testSalvarFotoPerfil_Sucesso() throws PomboException {
        MultipartFile fotoMock = mock(MultipartFile.class);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(imagemService.processarImagem(fotoMock)).thenReturn("imagemBase64");

        usuarioService.salvarFotoPerfil(fotoMock, 1L);

        verify(usuarioRepository, times(1)).save(usuario);
        assertThat(usuario.getFoto()).isEqualTo("imagemBase64");
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar uma foto para usuário inexistente")
    public void testSalvarFotoPerfil_UsuarioNaoEncontrado() {
        MultipartFile fotoMock = mock(MultipartFile.class);
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.salvarFotoPerfil(fotoMock, 999L))
                .isInstanceOf(PomboException.class)
                .hasMessageContaining("Usuário não encontrado!");
    }
}
