package com.pombo.pombo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.seletor.PruuSeletor;
import com.pombo.pombo.service.PruuService;
import com.pombo.pombo.exception.PomboException;

@RestController
@RequestMapping("/api/pruus")
public class PruuController {

    @Autowired
    private PruuService pruuService;

    @GetMapping
    public List<Pruu> listarTodos() {
        return pruuService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pruu> buscarPorId(@PathVariable Long id) throws PomboException {
        Pruu pruu = pruuService.buscarPorId(id);
        return ResponseEntity.ok(pruu);
    }

    @PostMapping
    public ResponseEntity<Pruu> criarPruu(@RequestBody Pruu novoPruu) {
        Pruu pruuCriado = pruuService.criarPruu(novoPruu);
        return ResponseEntity.status(201).body(pruuCriado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pruu> atualizarPruu(@PathVariable Long id, @RequestBody Pruu pruuAtualizado) throws PomboException {
        Pruu pruu = pruuService.atualizarPruu(id, pruuAtualizado);
        return ResponseEntity.ok(pruu);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPruu(@PathVariable Long id) throws PomboException {
        pruuService.excluirPruu(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/filtros")
    public List<Pruu> listarComFiltros(@RequestBody PruuSeletor seletor) {
        return pruuService.listarComFiltros(seletor);
    }

}
