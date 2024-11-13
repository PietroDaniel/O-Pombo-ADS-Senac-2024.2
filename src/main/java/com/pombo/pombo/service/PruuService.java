package com.pombo.pombo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.pombo.pombo.utils.RSAEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.pombo.pombo.exception.PomboException;
import com.pombo.pombo.model.dto.PruuDTO;
import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.repository.PruuRepository;
import com.pombo.pombo.model.seletor.PruuSeletor;

@Service
public class PruuService {

    @Autowired
    private PruuRepository pruuRepository;

    @Autowired
    private RSAEncoder rsaEncoder;

    public Pruu criarPruu(Pruu novoPruu) throws PomboException {
        if (novoPruu.getTexto().length() > 300) {
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

        return Pruu.paraDTO(pruu, quantidadeLikes, quantidadeDenuncias, null, null);
    }


    public List<PruuDTO> listarComFiltros(PruuSeletor seletor, Long subjectId) {

        List<Pruu> pruus;

        if (seletor.temPaginacao()) {
            int pageNumber = seletor.getPagina();
            int pageSize = seletor.getLimite();

            PageRequest page = PageRequest.of(pageNumber - 1, pageSize);
            pruus = pruuRepository.findAll(seletor, page).toList();
        } else {
            pruus = pruuRepository.findAll(seletor);
        }
        pruus = removerPruusDeletadosBloqueados(pruus);

        if (seletor.isEstaCurtido()) {
            pruus = pruus.stream()
                    .filter(pruu -> pruu.getLikedByUsers().stream()
                            .anyMatch(usuario -> usuario.getId().equals(subjectId)))
                    .collect(Collectors.toList());
        }

        return converterParaDTO(pruus);
    }

    public void excluirPruu(String pruuid, Long usuarioID) throws PomboException {

        Pruu pruu = pruuRepository.findById(pruuid).orElseThrow(() -> new PomboException("Pruu não encontrada!"));

        if (!pruu.getUsuario().getId().equals(usuarioID)) {
            throw new PomboException("Você não pode excluir um Pruu de outro usuário!");
        }

        pruu.setExcluido(true);
        pruuRepository.save(pruu);

    }

    public List<Pruu> removerPruusDeletadosBloqueados(List<Pruu> pruus) {
        return pruus.stream()
                .filter(pruu -> !pruu.isBloqueado() && !pruu.isExcluido())
                .collect(Collectors.toList());
    }

    public List<PruuDTO> converterParaDTO(List<Pruu> pruus) {

        List<PruuDTO> dtos = new ArrayList<>();

        for (Pruu p : pruus) {
            String pruuImagem = null;
            String usuarioFotoPerfil = null;

            p.setTexto(rsaEncoder.decode(p.getTexto()));

            Integer quantidadeLikes = p.getLikedByUsers().size();
            Integer quantidadeDenuncias = p.getDenuncias().size();

            if (p.getImagem() != null) {
                pruuImagem = null; //TODO
            }

            if (p.getUsuario().getFoto() != null) {
                usuarioFotoPerfil = null; //TODO
            }

            PruuDTO dto = Pruu.paraDTO(p, quantidadeLikes, quantidadeDenuncias, pruuImagem, usuarioFotoPerfil);
            dtos.add(dto);
        }
        return dtos;
    }
}