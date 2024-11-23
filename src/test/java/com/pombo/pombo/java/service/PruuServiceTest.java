package com.pombo.pombo.java.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.pombo.pombo.exception.PomboException;
import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.model.repository.PruuRepository;
import com.pombo.pombo.model.repository.UsuarioRepository;
import com.pombo.pombo.service.ImagemService;
import com.pombo.pombo.service.PruuService;
import com.pombo.pombo.utils.RSAEncoder;

@ExtendWith(MockitoExtension.class)
public class PruuServiceTest {

    @Mock
    private PruuRepository pruuRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RSAEncoder rsaEncoder;

    @Mock
    private ImagemService imagemService;

    @InjectMocks
    private PruuService pruuService;

    private Pruu pruu;
    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João");
        usuario.setEmail("joao@example.com");

        pruu = new Pruu();
        pruu.setId("pruu-01");
        pruu.setTexto("Texto de Criptografado");
        pruu.setUsuario(usuario);
        pruu.setLikedByUsers(new ArrayList<>());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar Pruu com texto inválido")
    public void testCriarPruuComTextoInvalido() {
        pruu.setTexto("A".repeat(301)); 

        assertThatThrownBy(() -> pruuService.criarPruu(pruu))
                .isInstanceOf(PomboException.class)
                .hasMessageContaining("O texto deve ter entre 1 e 300 caracteres");

        verify(pruuRepository, never()).save(any(Pruu.class));
    }

    @Test
    @DisplayName("Deve salvar a foto do Pruu com sucesso")
    public void testSalvarFotoPruuComSucesso() throws PomboException {
        MultipartFile fotoMock = mock(MultipartFile.class);
        when(imagemService.processarImagem(fotoMock)).thenReturn("base64Imagem");
        when(pruuRepository.findById("pruu-01")).thenReturn(Optional.of(pruu));
        when(pruuRepository.save(any(Pruu.class))).thenReturn(pruu);

        pruuService.salvarFotoPruu(fotoMock, "pruu-01", usuario.getId());

        assertThat(pruu.getFoto()).isEqualTo("base64Imagem");
        verify(pruuRepository, times(1)).save(pruu);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar foto para Pruu inexistente")
    public void testSalvarFotoPruuInexistente() {
        MultipartFile fotoMock = mock(MultipartFile.class);

        when(pruuRepository.findById("pruu-01")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pruuService.salvarFotoPruu(fotoMock, "pruu-01", usuario.getId()))
                .isInstanceOf(PomboException.class)
                .hasMessageContaining("Pruu não encontrado");

        verify(pruuRepository, never()).save(any(Pruu.class));
    }

    @Test
    @DisplayName("Deve dar like em um Pruu com sucesso")
    public void testDarLikeEmPruuComSucesso() throws PomboException {
        when(pruuRepository.findById("pruu-01")).thenReturn(Optional.of(pruu));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(pruuRepository.save(any(Pruu.class))).thenReturn(pruu);

        pruuService.darLike(1L, "pruu-01");

        assertThat(pruu.getLikedByUsers()).contains(usuario);
        verify(pruuRepository, times(1)).save(pruu);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar um Pruu inexistente por ID")
    public void testBuscarPruuPorIdInvalido() {
        when(pruuRepository.findById("id-invalido")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pruuService.buscarPorId("id-invalido"))
                .isInstanceOf(PomboException.class)
                .hasMessageContaining("Pruu não encontrado");

        verify(pruuRepository, times(1)).findById("id-invalido");
    }

    @Test
    @DisplayName("Deve excluir um Pruu com sucesso")
    public void testExcluirPruuComSucesso() throws PomboException {
        when(pruuRepository.findById("pruu-01")).thenReturn(Optional.of(pruu));

        pruuService.excluirPruu("pruu-01", usuario.getId());

        assertThat(pruu.isExcluido()).isTrue();
        verify(pruuRepository, times(1)).save(pruu);
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir um Pruu de outro usuário")
    public void testExcluirPruuDeOutroUsuario() {
        Usuario outroUsuario = new Usuario();
        outroUsuario.setId(2L);
        pruu.setUsuario(outroUsuario);

        when(pruuRepository.findById("pruu-01")).thenReturn(Optional.of(pruu));

        assertThatThrownBy(() -> pruuService.excluirPruu("pruu-01", usuario.getId()))
                .isInstanceOf(PomboException.class)
                .hasMessageContaining("Você não pode excluir um Pruu de outro usuário!");

        verify(pruuRepository, never()).save(any(Pruu.class));
    }
}
