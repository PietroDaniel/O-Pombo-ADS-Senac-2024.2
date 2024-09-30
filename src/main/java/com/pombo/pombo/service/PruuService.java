package com.pombo.pombo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pombo.pombo.exception.PomboException;
import com.pombo.pombo.model.dto.PruuDTO;
import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.repository.PruuRepository;
import com.pombo.pombo.model.seletor.PruuSeletor;

import jakarta.persistence.criteria.Predicate;

@Service
public class PruuService {

    @Autowired
    private PruuRepository pruuRepository;

    public List<Pruu> listarTodos() {
        return pruuRepository.findAll();
    }

    public Pruu buscarPorId(String uuid) throws PomboException {
        return pruuRepository.findById(uuid)
                .orElseThrow(() -> new PomboException("Pruu não encontrado"));
    }

    public Pruu criarPruu(Pruu novoPruu) {
        return pruuRepository.save(novoPruu);
    }

    public Pruu atualizarPruu(String uuid, Pruu pruuAtualizado) throws PomboException {
        Pruu pruu = buscarPorId(uuid);
        pruu.setTexto(pruuAtualizado.getTexto());
        pruu.setImagem(pruuAtualizado.getImagem());
        pruu.setBloqueado(pruuAtualizado.isBloqueado());
        return pruuRepository.save(pruu);
    }

    public void excluirPruu(String uuid) throws PomboException {
        Pruu pruu = buscarPorId(uuid);
        if (!pruu.isBloqueado()) {
            pruu.setBloqueado(true);
            pruuRepository.save(pruu);
        } else {
            throw new PomboException("Pruu já está bloqueado");
        }
    }

    public List<Pruu> listarComFiltros(PruuSeletor seletor) {
        return pruuRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (seletor.getTexto() != null && !seletor.getTexto().isEmpty()) {
                predicates.add(cb.like(root.get("texto"), "%" + seletor.getTexto() + "%"));
            }

            if (seletor.getDataInicioCriacao() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dataHoraCriacao"), seletor.getDataInicioCriacao()));
            }

            if (seletor.getDataFimCriacao() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dataHoraCriacao"), seletor.getDataFimCriacao()));
            }

            if (seletor.getQuantidadeMinimaLikes() != null) {
                predicates
                        .add(cb.greaterThanOrEqualTo(root.get("quantidadeLikes"), seletor.getQuantidadeMinimaLikes()));
            }

            if (seletor.getBloqueado() != null) {
                predicates.add(cb.equal(root.get("bloqueado"), seletor.getBloqueado()));
            }

            if (seletor.getUsuarioUuid() != null && !seletor.getUsuarioUuid().isEmpty()) {
                predicates.add(cb.equal(root.get("usuario").get("uuid"), seletor.getUsuarioUuid()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

    public PruuDTO gerarRelatorioPruu(String uuid) throws PomboException {
        Pruu pruu = pruuRepository.findById(uuid)
                .orElseThrow(() -> new PomboException("Pruu não encontrado"));

        PruuDTO dto = new PruuDTO();
        String texto = pruu.isBloqueado() ? "Bloqueado pelo administrador" : pruu.getTexto();
        dto.setTexto(texto);
        dto.setQuantidadeLikes(pruu.getQuantidadeLikes());
        dto.setNomeUsuario(pruu.getUsuario().getNome());
        dto.setUuidUsuario(pruu.getUsuario().getId().toString());
        dto.setQuantidadeDenuncias(pruu.getDenuncias().size());

        return dto;
    }

    
}