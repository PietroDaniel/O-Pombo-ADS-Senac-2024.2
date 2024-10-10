package com.pombo.pombo.java.model.repository;

import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.model.repository.PruuRepository;
import com.pombo.pombo.model.repository.UsuarioRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

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
        // Inicializando um usuário para ser utilizado nos testes de inserção de Pruu
        usuario = new Usuario();
        usuario.setNome("João");
        usuario.setEmail("joao@example.com");
        usuario.setCpf("123.456.789-09");
        usuarioRepository.save(usuario);
    }

    @Test
    public void testSalvarDadosCorretos() {
        // Teste de inserção de um Pruu com todos os dados corretos
        Pruu pruu = new Pruu();
        pruu.setTexto("Texto válido");
        pruu.setUsuario(usuario);

        Pruu pruuSalvo = pruuRepository.save(pruu);
        
        // Verificações
        assertNotNull(pruuSalvo);
        assertEquals("Texto válido", pruuSalvo.getTexto());
        assertEquals(usuario.getId(), pruuSalvo.getUsuario().getId());
    }

    @Test
    public void testSalvarTextoInvalidoNulo() {
        // Teste para verificar inserção de um Pruu com texto nulo
        Pruu pruu = new Pruu();
        pruu.setTexto(null); // Texto nulo
        pruu.setUsuario(usuario);

        assertThatThrownBy(() -> pruuRepository.save(pruu))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessageContaining("O texto do pruu é obrigatório");
    }

    @Test
    public void testSalvarTextoInvalidoMaiorQue300Caracteres() {
        // Teste para verificar inserção de um Pruu com texto maior que 300 caracteres
        Pruu pruu = new Pruu();
        pruu.setTexto("A".repeat(301)); // Texto com 301 caracteres
        pruu.setUsuario(usuario);

        assertThatThrownBy(() -> pruuRepository.save(pruu))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessageContaining("O texto deve ter entre 1 e 300 caracteres");
    }

    @Test
    public void testAtualizarDadosCorretos() {
        // Teste para verificar a atualização de um Pruu existente com dados corretos
        Pruu pruu = new Pruu();
        pruu.setTexto("Texto original");
        pruu.setUsuario(usuario);
        Pruu pruuSalvo = pruuRepository.save(pruu);

        // Atualizando o texto do Pruu
        pruuSalvo.setTexto("Texto atualizado");
        Pruu pruuAtualizado = pruuRepository.save(pruuSalvo);

        assertNotNull(pruuAtualizado);
        assertEquals("Texto atualizado", pruuAtualizado.getTexto());
    }

    @Test
    public void testAtualizarTextoInvalidoMaiorQue300Caracteres() {
        // Teste para verificar a atualização de um Pruu com texto inválido (> 300 caracteres)
        Pruu pruu = new Pruu();
        pruu.setTexto("Texto original");
        pruu.setUsuario(usuario);
        Pruu pruuSalvo = pruuRepository.save(pruu);

        pruuSalvo.setTexto("A".repeat(301)); // Atualizando com texto inválido

        assertThatThrownBy(() -> pruuRepository.save(pruuSalvo))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessageContaining("O texto deve ter entre 1 e 300 caracteres");
    }

    @Test
    public void testExcluirIdCorretoExistente() {
        // Teste para verificar a exclusão de um Pruu existente
        Pruu pruu = new Pruu();
        pruu.setTexto("Texto para exclusão");
        pruu.setUsuario(usuario);
        Pruu pruuSalvo = pruuRepository.save(pruu);

        pruuRepository.deleteById(pruuSalvo.getUuid());

        assertThatThrownBy(() -> pruuRepository.findById(pruuSalvo.getUuid()).orElseThrow(() -> new RuntimeException("Pruu não encontrado")))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Pruu não encontrado");
    }

    @Test
    public void testExcluirIdIncorretoInexistente() {
        // Teste para verificar a exclusão de um Pruu inexistente
        String uuidInexistente = "uuid-nao-existente";
        assertThatThrownBy(() -> pruuRepository.deleteById(uuidInexistente))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Pruu não encontrado");
    }

    @Test
    public void testPesquisarTodos() {
        // Teste para verificar a listagem de todos os Pruus
        Pruu pruu1 = new Pruu();
        pruu1.setTexto("Pruu 1");
        pruu1.setUsuario(usuario);

        Pruu pruu2 = new Pruu();
        pruu2.setTexto("Pruu 2");
        pruu2.setUsuario(usuario);

        pruuRepository.save(pruu1);
        pruuRepository.save(pruu2);

        assertEquals(2, pruuRepository.findAll().size());
    }
}
