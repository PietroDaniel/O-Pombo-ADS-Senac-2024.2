package com.pombo.pombo.controller;

import com.pombo.pombo.auth.AuthenticationService;
import com.pombo.pombo.exception.PomboException;
import com.pombo.pombo.model.dto.DenunciaDTO;
import com.pombo.pombo.model.entity.Denuncia;
import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.model.enums.Role;
import com.pombo.pombo.model.enums.SituacaoDenuncia;
import com.pombo.pombo.model.seletor.DenunciaSeletor;
import com.pombo.pombo.service.DenunciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/denuncias")
public class DenunciaController {

    @Autowired
    private DenunciaService denunciaService;

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/pruu/{pruuId}")
    public List<DenunciaDTO> buscarDenunciasPorPruu(@PathVariable String pruuId) throws PomboException {

        Usuario subject = authenticationService.getAuthenticatedUser();

        if (subject.getRole() == Role.ADMIN) {
            return denunciaService.buscarDenunciasPorPruu(pruuId);
        } else {
            throw new PomboException("Usuário não autorizado!");
        }
    }

    @PatchMapping("/admin/atualizar-situacao/{denunciaId}/{novaSituacaoString}")
    public ResponseEntity<Void> atualizarSituacao(@PathVariable String denunciaId, @PathVariable String novaSituacaoString) throws PomboException {

        Usuario subject = authenticationService.getAuthenticatedUser();
        SituacaoDenuncia novaSituacao = null;

        if (novaSituacaoString.equalsIgnoreCase("aceita")) {
            novaSituacao = SituacaoDenuncia.ACEITA;
        } else if (novaSituacaoString.equalsIgnoreCase("pendente")) {
            novaSituacao = SituacaoDenuncia.PENDENTE;
        } else if (novaSituacaoString.equalsIgnoreCase("recusada")) {
            novaSituacao = SituacaoDenuncia.RECUSADA;
        } else {
            throw new PomboException("Situação inválida!");
        }

        if (subject.getRole() == Role.ADMIN) {
            denunciaService.atualizarSituacao(denunciaId, novaSituacao);
            return ResponseEntity.ok().build();
        } else {
            throw new PomboException("Usuário não autorizado!");
        }

    }

    @PostMapping
    public ResponseEntity<Denuncia> criarDenuncia(@RequestBody Denuncia novaDenuncia) throws PomboException {

        Usuario subject = authenticationService.getAuthenticatedUser();

        if (subject.getRole() == Role.USER) {

            novaDenuncia.setDenunciante(subject);
            Denuncia denunciaCriada = denunciaService.criarDenuncia(novaDenuncia);
            return ResponseEntity.status(201).body(denunciaCriada);
        } else {
            throw new PomboException("Administradores não podem criar Pruus!");
        }

    }

    @PostMapping("/filtros")
    public List<DenunciaDTO> listarComFiltros(@RequestBody DenunciaSeletor seletor) throws PomboException {

        Usuario subject = authenticationService.getAuthenticatedUser();

        if (subject.getRole() == Role.ADMIN) {
            return denunciaService.listarComFiltro(seletor);
        } else {
            throw new PomboException("Usuário não autorizado!");
        }
    }

    @DeleteMapping("/{denunciaId}")
    public ResponseEntity<Void> excluirDenuncia(@PathVariable String denunciaId) throws PomboException {

        Usuario subject = authenticationService.getAuthenticatedUser();

        denunciaService.excluirDenuncia(denunciaId, subject.getId());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("{denunciaId}")
    public Denuncia buscarDenunciaPorId(@PathVariable String denunciaId) throws PomboException {
        Usuario subject = authenticationService.getAuthenticatedUser();

        if (subject.getRole() == Role.ADMIN) {
            return denunciaService.procurarPorId(denunciaId);
        } else {
            throw new PomboException("Usuário não autorizado!");
        }
    }
}
