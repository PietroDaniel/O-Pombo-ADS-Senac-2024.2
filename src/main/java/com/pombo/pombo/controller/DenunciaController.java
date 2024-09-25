package com.pombo.pombo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pombo.pombo.model.entity.Denuncia;
import com.pombo.pombo.service.DenunciaService;
import com.pombo.pombo.exception.PomboException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/denuncias")
public class DenunciaController {

    @Autowired
    private DenunciaService denunciaService;

    @GetMapping("/pruu/{pruuUuid}")
    public List<Denuncia> buscarDenunciasPorPruu(@PathVariable UUID pruuUuid) {
        return denunciaService.buscarDenunciasPorPruu(pruuUuid);
    }

    @PutMapping("/{denunciaUuid}")
    public ResponseEntity<Void> atualizarSituacao(@PathVariable UUID denunciaUuid,
                                                  @RequestParam Denuncia.SituacaoDenuncia situacao) throws PomboException {
        denunciaService.atualizarSituacao(denunciaUuid, situacao);
        return ResponseEntity.ok().build();
    }
}
