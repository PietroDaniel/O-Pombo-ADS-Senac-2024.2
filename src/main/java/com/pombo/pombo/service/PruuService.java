package com.pombo.pombo.service;

import java.util.ArrayList;
import java.util.List;

import com.pombo.pombo.utils.RSAEncoder;
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

    @Autowired
    private RSAEncoder rsaEncoder;

    public Pruu criarPruu(Pruu novoPruu) throws PomboException {
        if(novoPruu.getTexto().length() > 300) {
            throw new PomboException("O texto deve ter entre 1 e 350 caracteres");
        }

        String textoCriptografado = rsaEncoder.encode(novoPruu.getTexto());

        novoPruu.setTexto(textoCriptografado);

        return pruuRepository.save(novoPruu);
    }

    public List<Pruu> listarTodos() {
        return pruuRepository.findAll();
    }

    public PruuDTO buscarPorId(String uuid) throws PomboException {
        Pruu pruu = pruuRepository.findById(uuid).orElseThrow(() -> new PomboException("Pruu não encontrado"));
        pruu.setTexto(rsaEncoder.decode(pruu.getTexto()));

        Integer quantidadeLikes = pruu.getLikedByUsers().size();
        Integer quantidadeDenuncias = pruu.getDenuncias().size();

        return Pruu.paraDTO(pruu, quantidadeLikes, quantidadeDenuncias);
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
    };

    public void excluirPruu(String uuid) throws PomboException {
        Pruu pruu = buscarPorId(uuid);
        if (!pruu.isBloqueado()) {
            pruu.setBloqueado(true);
            pruuRepository.save(pruu);
        } else {
            throw new PomboException("Pruu já está bloqueado");
        }
    }


    public PruuDTO gerarRelatorioPruu(String uuid) throws PomboException {
        Pruu pruu = pruuRepository.findById(uuid)
                .orElseThrow(() -> new PomboException("Pruu não encontrado"));

        PruuDTO dto = new PruuDTO();
        String texto = pruu.isBloqueado() ? "Bloqueado pelo administrador" : pruu.getTexto();
        dto.setTexto(texto);
        dto.setNomeUsuario(pruu.getUsuario().getNome());
        dto.setUuidUsuario(pruu.getUsuario().getId().toString());
        dto.setQuantidadeDenuncias(pruu.getDenuncias().size());

        return dto;
    }

    
}