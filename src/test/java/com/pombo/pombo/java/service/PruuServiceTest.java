package com.pombo.pombo.java.service;

import com.pombo.pombo.exception.PomboException;
import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.model.repository.PruuRepository;
import com.pombo.pombo.model.repository.UsuarioRepository;
import com.pombo.pombo.service.PruuService;

import jakarta.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class PruuServiceTest {

    @Mock
    private PruuRepository pruuRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PruuService pruuService;

    private Pruu pruu;
    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inicializando objetos mock para os testes
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João");
        usuario.setEmail("joao@example.com");
        usuario.setCpf("123.456.789-09");

        pruu = new Pruu();
        pruu.setTexto("Texto de teste");
        pruu.setUsuario(usuario);
    }

    @Test
    @DisplayName("Deve salvar um Pruu com sucesso")
    public void testSalvarPruuComSucesso() {
        // Simulando o comportamento do repositório
        when(pruuRepository.save(any(Pruu.class))).thenReturn(pruu);

        Pruu pruuSalvo = pruuService.criarPruu(pruu);

        // Verificações
        assertNotNull(pruuSalvo);
        assertEquals("Texto de teste", pruuSalvo.getTexto());
        verify(pruuRepository, times(1)).save(any(Pruu.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar salvar Pruu com texto inválido")
    public void testSalvarPruuComTextoInvalido() {
        pruu.setTexto(""); // Texto inválido (vazio)

        assertThatThrownBy(() -> pruuService.criarPruu(pruu))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessageContaining("O texto do pruu é obrigatório");

        verify(pruuRepository, times(0)).save(any(Pruu.class));
    }

    @Test
    @DisplayName("Deve buscar um Pruu por ID com sucesso")
    public void testBuscarPruuPorIdComSucesso() throws PomboException {
        // Simulando a busca de um Pruu existente
        when(pruuRepository.findById(anyString())).thenReturn(Optional.of(pruu));

        Pruu pruuEncontrado = pruuService.buscarPorId("uuid-pruu");

        assertNotNull(pruuEncontrado);
        assertEquals(pruu.getTexto(), pruuEncontrado.getTexto());
        verify(pruuRepository, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar um Pruu com UUID inválido")
    public void testBuscarPruuPorIdInvalido() {
        // Simulando a ausência de um Pruu para o UUID fornecido
        when(pruuRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pruuService.buscarPorId("uuid-invalido"))
            .isInstanceOf(PomboException.class)
            .hasMessageContaining("Pruu não encontrado");

        verify(pruuRepository, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("Deve atualizar um Pruu com sucesso")
    public void testAtualizarPruuComSucesso() throws PomboException {
        when(pruuRepository.findById(anyString())).thenReturn(Optional.of(pruu));

        Pruu pruuAtualizado = new Pruu();
        pruuAtualizado.setTexto("Texto atualizado");
        pruuAtualizado.setImagem("imagem.png");

        when(pruuRepository.save(any(Pruu.class))).thenReturn(pruuAtualizado);

        Pruu resultado = pruuService.atualizarPruu("uuid-pruu", pruuAtualizado);

        assertNotNull(resultado);
        assertEquals("Texto atualizado", resultado.getTexto());
        assertEquals("imagem.png", resultado.getImagem());
        verify(pruuRepository, times(1)).save(any(Pruu.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar um Pruu inexistente")
    public void testAtualizarPruuInvalido() {
        when(pruuRepository.findById(anyString())).thenReturn(Optional.empty());

        Pruu pruuAtualizado = new Pruu();
        pruuAtualizado.setTexto("Texto atualizado");

        assertThatThrownBy(() -> pruuService.atualizarPruu("uuid-invalido", pruuAtualizado))
            .isInstanceOf(PomboException.class)
            .hasMessageContaining("Pruu não encontrado");

        verify(pruuRepository, times(0)).save(any(Pruu.class));
    }

    @Test
    @DisplayName("Deve bloquear um Pruu com sucesso")
    public void testBloquearPruuComSucesso() throws PomboException {
        pruu.setBloqueado(false);

        when(pruuRepository.findById(anyString())).thenReturn(Optional.of(pruu));

        pruuService.excluirPruu("uuid-pruu");

        assertTrue(pruu.isBloqueado());
        verify(pruuRepository, times(1)).save(any(Pruu.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar bloquear um Pruu já bloqueado")
    public void testBloquearPruuJaBloqueado() throws PomboException {
        pruu.setBloqueado(true);

        when(pruuRepository.findById(anyString())).thenReturn(Optional.of(pruu));

        assertThatThrownBy(() -> pruuService.excluirPruu("uuid-pruu"))
            .isInstanceOf(PomboException.class)
            .hasMessageContaining("Pruu já está bloqueado");

        verify(pruuRepository, times(0)).save(any(Pruu.class));
    }
}
