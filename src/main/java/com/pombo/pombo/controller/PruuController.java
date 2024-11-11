package com.pombo.pombo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pombo.pombo.exception.PomboException;
import com.pombo.pombo.model.dto.PruuDTO;
import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.seletor.PruuSeletor;
import com.pombo.pombo.service.PruuService;

@RestController
@RequestMapping("/api/pruus")
public class PruuController {

    @Autowired
    private PruuService pruuService;

    @GetMapping
    public List<Pruu> listarTodos() {
        return pruuService.listarTodos();
    }

    public ResponseEntity<PruuDTO> buscarPorId(@PathVariable String uuid) throws PomboException {
        PruuDTO pruu = pruuService.buscarPorId(uuid);
        return ResponseEntity.ok(pruu);
    }

    @PostMapping
    public ResponseEntity<Pruu> criarPruu(@RequestBody Pruu novoPruu) throws PomboException {
        Pruu pruuCriado = pruuService.criarPruu(novoPruu);
        return ResponseEntity.status(201).body(pruuCriado);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deletarPruu(@PathVariable String uuid) throws PomboException {
        pruuService.excluirPruu(uuid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/filtros")
    public List<Pruu> listarComFiltros(@RequestBody PruuSeletor seletor) {
        return pruuService.listarComFiltros(seletor);
    }

    @PutMapping("/bloquear/{uuid}")
    public ResponseEntity<Void> bloquearPruu(@PathVariable String uuid) throws PomboException {
        pruuService.excluirPruu(uuid); 
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/relatorio/{uuid}")
    public ResponseEntity<PruuDTO> gerarRelatorioPruu(@PathVariable String uuid) throws PomboException {
        PruuDTO pruuDTO = pruuService.gerarRelatorioPruu(uuid);
        return ResponseEntity.ok(pruuDTO);
    }

}
