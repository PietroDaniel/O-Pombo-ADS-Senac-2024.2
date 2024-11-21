package com.pombo.pombo.java.model.repository;

import com.pombo.pombo.java.factories.UsuarioFactory;
import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.model.repository.UsuarioRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Não deve ser possível criar um usuário com nome com mais de 255 caracteres")
    public void testeCriar$nomeMaiorQue255Caracteres() {
        Usuario usuario = UsuarioFactory.criarUsuario();
        usuario.setNome("A".repeat(256));

        assertThatThrownBy(() -> usuarioRepository.saveAndFlush(usuario))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("O nome deve conter no máximo 255 caracteres");
    }

    @Test
    @DisplayName("Email deve ser válido")
    public void testeCriar$emailDeveSerValido() {
        Usuario usuario = UsuarioFactory.criarUsuario();
        usuario.setEmail("email_invalido");

        assertThatThrownBy(() -> usuarioRepository.saveAndFlush(usuario))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Email deve ser válido");
    }

    @Test
    @DisplayName("Deve aceitar apenas CPFs válidos")
    public void testeCriar$cpfDeveSerValido() {
        Usuario usuario = UsuarioFactory.criarUsuario();
        usuario.setCpf("123.456.789-00");

        assertThatThrownBy(() -> usuarioRepository.saveAndFlush(usuario))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("CPF deve ser válido");
    }

    @Test
    @DisplayName("A senha é obrigatória")
    public void testeCriar$senhaDeveSerObrigatoria() {
        Usuario usuario = UsuarioFactory.criarUsuario();
        usuario.setPassword(null);

        assertThatThrownBy(() -> usuarioRepository.saveAndFlush(usuario))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("A senha é obrigatória");
    }
}
