package com.pombo.pombo.controller;

import com.pombo.pombo.exception.PomboException;
import com.pombo.pombo.model.dto.DenunciaDTO;
import com.pombo.pombo.model.entity.Denuncia;
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

    @GetMapping("/pruu/{pruuId}")
    public ResponseEntity<List<DenunciaDTO>> buscarDenunciasPorPruu(@PathVariable String pruuId) {
        List<DenunciaDTO> denuncias = denunciaService.buscarDenunciasPorPruu(pruuId)
                .stream()
                .map(Denuncia::paraDenunciaDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(denuncias);
    }

    @PutMapping("/{denunciaId}")
    public ResponseEntity<Void> atualizarSituacao(
            @PathVariable String denunciaId,
            @RequestParam SituacaoDenuncia situacao) throws PomboException {
        denunciaService.atualizarSituacao(denunciaId, situacao);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<DenunciaDTO> criarDenuncia(@RequestBody Denuncia novaDenuncia) throws PomboException {
        Denuncia denunciaCriada = denunciaService.criarDenuncia(novaDenuncia);
        return ResponseEntity.status(HttpStatus.CREATED).body(Denuncia.paraDenunciaDTO(denunciaCriada));
    }

    @PostMapping("/filtros")
    public ResponseEntity<List<DenunciaDTO>> listarComFiltros(@RequestBody DenunciaSeletor seletor) {
        List<DenunciaDTO> denuncias = denunciaService.listarComFiltro(seletor);
        return ResponseEntity.ok(denuncias);
    }

    @DeleteMapping("/{denunciaId}")
    public ResponseEntity<Void> excluirDenuncia(@PathVariable String denunciaId, @RequestParam Long usuarioId) throws PomboException {
        denunciaService.excluirDenuncia(denunciaId, usuarioId);
        return ResponseEntity.noContent().build();
    }
}
