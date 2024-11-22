package com.pombo.pombo.java.model.repository;

import com.pombo.pombo.model.dto.DenunciaDTO;
import com.pombo.pombo.model.entity.Denuncia;
import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.model.enums.MotivoDenuncia;
import com.pombo.pombo.model.repository.DenunciaRepository;
import com.pombo.pombo.model.repository.PruuRepository;
import com.pombo.pombo.model.repository.UsuarioRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class DenunciaRepositoryTest {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private PruuRepository pruuRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuarioTest;
    private Pruu pruuTest;

    @BeforeEach
    public void setUp() {
        // Criação do usuário de teste
        usuarioTest = new Usuario();
        usuarioTest.setNome("Test");
        usuarioTest.setEmail("test@mail.com");
        usuarioTest.setCpf("34348230005");
        usuarioTest.setPassword("test123");
        usuarioRepository.save(usuarioTest);

        // Criação do Pruu de teste
        pruuTest = new Pruu();
        pruuTest.setTexto("Mensagem de teste");
        pruuTest.setUsuario(usuarioTest);
        pruuRepository.save(pruuTest);

        // Criação de uma denúncia de teste
        Denuncia denunciaTeste = new Denuncia();
        denunciaTeste.setDenunciante(usuarioTest);
        denunciaTeste.setPruu(pruuTest);
        denunciaTeste.setMotivo(MotivoDenuncia.SPAM);
        denunciaRepository.save(denunciaTeste);
    }

    @AfterEach
    public void tearDown() {
        denunciaRepository.deleteAll();
        pruuRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar uma denúncia com sucesso")
    public void testCriarDenunciaSucesso() {
        Denuncia denunciaTeste = new Denuncia();
        denunciaTeste.setDenunciante(usuarioTest);
        denunciaTeste.setPruu(pruuTest);
        denunciaTeste.setMotivo(MotivoDenuncia.OFENSIVO);

        Denuncia denunciaSalva = denunciaRepository.saveAndFlush(denunciaTeste);
        assertNotNull(denunciaSalva);
        assertEquals(MotivoDenuncia.OFENSIVO, denunciaSalva.getMotivo());
    }

    @Test
    @DisplayName("Deve retornar denúncias associadas a um Pruu específico pelo ID")
    public void testFindByPruuId() {
        List<Denuncia> denuncias = denunciaRepository.findByPruuId(pruuTest.getId());

        assertFalse(denuncias.isEmpty());

        for (Denuncia denuncia : denuncias) {
            assertEquals(pruuTest.getId(), denuncia.getPruu().getId());
        }
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver denúncias associadas ao Pruu")
    public void testFindByPruuIdEmpty() {
        List<Denuncia> denuncias = denunciaRepository.findByPruuId("idInexistente");

        assertTrue(denuncias.isEmpty());
    }
}
