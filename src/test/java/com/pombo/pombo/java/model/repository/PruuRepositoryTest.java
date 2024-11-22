package com.pombo.pombo.java.model.repository;

import com.pombo.pombo.java.factories.PruuFactory;
import com.pombo.pombo.java.factories.UsuarioFactory;
import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.model.repository.PruuRepository;
import com.pombo.pombo.model.repository.UsuarioRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
public class PruuRepositoryTest {

    @Autowired
    private PruuRepository pruuRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        usuario = UsuarioFactory.criarUsuario();
        usuarioRepository.saveAndFlush(usuario); // Salvar o usuário para associá-lo aos Pruus
    }

    @Test
    @DisplayName("Deve salvar um Pruu com sucesso")
    public void testSalvarPruuComSucesso() {
        Pruu pruu = PruuFactory.criarPruu(usuario);

        Pruu pruuSalvo = pruuRepository.saveAndFlush(pruu);

        assertThat(pruuSalvo).isNotNull();
        assertThat(pruuSalvo.getId()).isNotNull();
        assertThat(pruuSalvo.getTexto()).isEqualTo(pruu.getTexto());
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar Pruu com texto vazio")
    public void testSalvarPruuTextoVazio() {
        Pruu pruu = PruuFactory.criarPruu(usuario);
        pruu.setTexto(""); // Texto inválido

        assertThatThrownBy(() -> pruuRepository.saveAndFlush(pruu))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("O texto do pruu é obrigatório");
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar Pruu com texto muito longo")
    public void testSalvarPruuTextoMuitoLongo() {
        Pruu pruu = PruuFactory.criarPruu(usuario);
        pruu.setTexto("A".repeat(351)); // Texto maior que o permitido

        assertThatThrownBy(() -> pruuRepository.saveAndFlush(pruu))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("O texto deve ter entre 1 e 300 caracteres");
    }

    @Test
    @DisplayName("Deve encontrar um Pruu por ID")
    public void testBuscarPruuPorId() {
        Pruu pruu = PruuFactory.criarPruu(usuario);
        Pruu pruuSalvo = pruuRepository.saveAndFlush(pruu);

        Optional<Pruu> pruuEncontrado = pruuRepository.findById(pruuSalvo.getId());

        assertThat(pruuEncontrado).isPresent();
        assertThat(pruuEncontrado.get().getTexto()).isEqualTo(pruu.getTexto());
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao buscar likes de um Pruu sem curtidas")
    public void testBuscarLikesDePruuSemCurtidas() {
        Pruu pruu = PruuFactory.criarPruu(usuario);
        Pruu pruuSalvo = pruuRepository.saveAndFlush(pruu);

        List<Usuario> likes = pruuSalvo.getLikedByUsers();

        assertThat(likes).isEmpty();
    }

    @Test
    @DisplayName("Deve excluir um Pruu com sucesso")
    public void testExcluirPruuComSucesso() {
        Pruu pruu = PruuFactory.criarPruu(usuario);
        Pruu pruuSalvo = pruuRepository.saveAndFlush(pruu);

        pruuRepository.deleteById(pruuSalvo.getId());

        Optional<Pruu> pruuExcluido = pruuRepository.findById(pruuSalvo.getId());
        assertThat(pruuExcluido).isEmpty();
    }
}
