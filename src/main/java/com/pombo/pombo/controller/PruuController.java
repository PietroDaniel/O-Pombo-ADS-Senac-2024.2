package com.pombo.pombo.controller;

import java.io.IOException;
import java.util.List;

import com.pombo.pombo.auth.AuthenticationService;
import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.model.enums.Role;

import jakarta.servlet.annotation.MultipartConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;

import com.pombo.pombo.exception.PomboException;
import com.pombo.pombo.model.dto.PruuDTO;
import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.seletor.PruuSeletor;
import com.pombo.pombo.service.PruuService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/pruus")
@MultipartConfig(fileSizeThreshold = 10485760) // 10MB

public class PruuController {

    @Autowired
    private PruuService pruuService;

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/salvar-foto-pruu")
    public void salvarFotoPruu(@RequestParam("foto") MultipartFile foto, @RequestParam("pruuId") String pruuId) throws IOException, PomboException {

        Usuario subject = authenticationService.getAuthenticatedUser();

        if (foto == null) {
            throw new PomboException("Arquivo inválido!");
        }

        pruuService.salvarFotoPruu(foto, pruuId, subject.getId());
    }

    @PostMapping
    public ResponseEntity<Pruu> criarPruu(@RequestBody Pruu novoPruu) throws PomboException {

        Usuario subject = authService.getAuthenticatedUser();

        if (subject.getRole() == Role.USER) {

            novoPruu.setUsuario(subject);
            Pruu pruuCriado = pruuService.criarPruu(novoPruu);
            return ResponseEntity.status(201).body(pruuCriado);
        } else {
            throw new PomboException("Administradores não podem criar Pruus!");
        }
    }

    @PostMapping("/dar-like/{pruuId}")
    public ResponseEntity<Void> darLike(@PathVariable String pruuId) throws PomboException {

        Usuario subject = authService.getAuthenticatedUser();

        if (subject.getRole() == Role.USER) {
            pruuService.darLike(subject.getId(), pruuId);
            return ResponseEntity.ok().build();
        } else {
            throw new PomboException("Administradores não podem dar likes em Pruus!");
        }
    }

    @GetMapping("/buscar-likes-pruu/{pruuId}")
    public List<Usuario> buscarLikesPruu(@PathVariable String pruuId) throws PomboException {
        return pruuService.buscarLikesPruu(pruuId);
    }

    @GetMapping("/{pruuId}")
    public ResponseEntity<PruuDTO> buscarPorId(@PathVariable String pruuId) throws PomboException {

        PruuDTO pruu = pruuService.buscarPorId(pruuId);
        return ResponseEntity.ok(pruu);
    }
    
    @PutMapping("/atualizar/{pruuId}")
    public ResponseEntity<Void> atualizarPruu(@PathVariable String pruuId, @RequestBody PruuDTO pruuDTO) throws PomboException {
        pruuService.atualizarPruu(pruuId, pruuDTO.getPruuConteudo());
        return ResponseEntity.noContent().build();
    }


//    @PostMapping("/filtros")
//    public List<PruuDTO> listarComFiltros(@RequestBody PruuSeletor seletor) throws PomboException {
//
//        Usuario usuarioAutenticado = authService.getAuthenticatedUser();
//
//        return (List<PruuDTO>) pruuService.listarComFiltros(seletor, usuarioAutenticado);
//    }
    

    @PostMapping("/filtros")
    public ResponseEntity<Page<PruuDTO>> listarComFiltros(
            @RequestBody PruuSeletor seletor) throws PomboException {

        Usuario usuarioAutenticado = authService.getAuthenticatedUser();

        Page<PruuDTO> pruus = pruuService.listarComFiltros(seletor, usuarioAutenticado);
        return ResponseEntity.ok(pruus);
    }
    
    

    @DeleteMapping("/{pruuId}")
    public ResponseEntity<Void> deletarPruu(@PathVariable String pruuId) throws PomboException {
        Usuario subject = authService.getAuthenticatedUser();
        pruuService.excluirPruu(pruuId, subject.getId());
        return ResponseEntity.noContent().build();
    }
}
