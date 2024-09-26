package com.pombo.pombo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pombo.pombo.exception.PomboException;
import com.pombo.pombo.model.entity.Denuncia;
import com.pombo.pombo.service.DenunciaService;

@RestController
@RequestMapping("/api/denuncias")
public class DenunciaController {

    @Autowired
    private DenunciaService denunciaService;

    @GetMapping("/pruu/{pruuString}")
    public List<Denuncia> buscarDenunciasPorPruu(@PathVariable String pruuString) {
        return denunciaService.buscarDenunciasPorPruu(pruuString);
    }

    @PutMapping("/{denunciaString}")
    public ResponseEntity<Void> atualizarSituacao(@PathVariable String denunciaString,
            @RequestParam Denuncia.SituacaoDenuncia situacao) throws PomboException {
        denunciaService.atualizarSituacao(denunciaString, situacao);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Denuncia> criarDenuncia(@RequestBody Denuncia novaDenuncia) throws PomboException {
        Denuncia denunciaCriada = denunciaService.criarDenuncia(novaDenuncia);
        return ResponseEntity.status(201).body(denunciaCriada);
    }
}
