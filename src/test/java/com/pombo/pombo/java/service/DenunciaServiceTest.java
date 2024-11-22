package com.pombo.pombo.java.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.pombo.pombo.exception.PomboException;
import com.pombo.pombo.model.dto.DenunciaDTO;
import com.pombo.pombo.model.entity.Denuncia;
import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.model.enums.MotivoDenuncia;
import com.pombo.pombo.model.enums.SituacaoDenuncia;
import com.pombo.pombo.model.repository.DenunciaRepository;
import com.pombo.pombo.model.repository.PruuRepository;
import com.pombo.pombo.service.DenunciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DenunciaServiceTest {

    @Mock
    private DenunciaRepository denunciaRepository;

    @Mock
    private PruuRepository pruuRepository;

    @InjectMocks
    private DenunciaService denunciaService;

    private Usuario denunciante;
    private Pruu pruu;
    private Denuncia denuncia;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuração de objetos básicos
        denunciante = new Usuario();
        denunciante.setId(1L);
        denunciante.setNome("Usuário Teste");

        pruu = new Pruu();
        pruu.setId("pruu-01");
        pruu.setTexto("Texto de teste");

        denuncia = new Denuncia();
        denuncia.setId("denuncia-01");
        denuncia.setDenunciante(denunciante);
        denuncia.setPruu(pruu);
        denuncia.setMotivo(MotivoDenuncia.SPAM);
        denuncia.setSituacao(SituacaoDenuncia.PENDENTE);
    }

    @Test
    @DisplayName("Deve criar uma denúncia com sucesso")
    public void testCriarDenuncia_Sucesso() throws PomboException {
        when(denunciaRepository.save(any(Denuncia.class))).thenReturn(denuncia);

        Denuncia novaDenuncia = denunciaService.criarDenuncia(denuncia);

        assertThat(novaDenuncia).isNotNull();
        assertThat(novaDenuncia.getId()).isEqualTo("denuncia-01");
        verify(denunciaRepository, times(1)).save(denuncia);
    }

    @Test
    @DisplayName("Deve lançar exceção ao procurar denúncia inexistente")
    public void testProcurarPorId_Falha() {
        when(denunciaRepository.findById("inexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> denunciaService.procurarPorId("inexistente"))
                .isInstanceOf(PomboException.class)
                .hasMessage("Esta denúncia não foi encontrada!");
    }

    @Test
    @DisplayName("Deve buscar denúncias por ID do Pruu")
    public void testBuscarDenunciasPorPruu() {
        when(denunciaRepository.findByPruuId("pruu-01")).thenReturn(Arrays.asList(denuncia));

        List<DenunciaDTO> denuncias = denunciaService.buscarDenunciasPorPruu("pruu-01");

        assertThat(denuncias).hasSize(1);
        assertThat(denuncias.get(0).getDenunciaId()).isEqualTo("denuncia-01");
        verify(denunciaRepository, times(1)).findByPruuId("pruu-01");
    }

    @Test
    @DisplayName("Deve atualizar situação da denúncia e bloquear o Pruu")
    public void testAtualizarSituacao_Aceita() throws PomboException {
        when(denunciaRepository.findById("denuncia-01")).thenReturn(Optional.of(denuncia));
        when(pruuRepository.save(any(Pruu.class))).thenReturn(pruu);

        denunciaService.atualizarSituacao("denuncia-01", SituacaoDenuncia.ACEITA);

        assertThat(denuncia.getSituacao()).isEqualTo(SituacaoDenuncia.ACEITA);
        assertThat(pruu.isBloqueado()).isTrue();
        verify(denunciaRepository, times(1)).save(denuncia);
        verify(pruuRepository, times(1)).save(pruu);
    }

    @Test
    @DisplayName("Deve excluir uma denúncia com sucesso")
    public void testExcluirDenuncia_Sucesso() throws PomboException {
        when(denunciaRepository.findById("denuncia-01")).thenReturn(Optional.of(denuncia));

        denunciaService.excluirDenuncia("denuncia-01", 1L);

        verify(denunciaRepository, times(1)).deleteById("denuncia-01");
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir denúncia de outro usuário")
    public void testExcluirDenuncia_OutroUsuario() {
        when(denunciaRepository.findById("denuncia-01")).thenReturn(Optional.of(denuncia));

        assertThatThrownBy(() -> denunciaService.excluirDenuncia("denuncia-01", 2L))
                .isInstanceOf(PomboException.class)
                .hasMessage("Você não pode excluir denúncias de outras pessoas!");
    }
}
