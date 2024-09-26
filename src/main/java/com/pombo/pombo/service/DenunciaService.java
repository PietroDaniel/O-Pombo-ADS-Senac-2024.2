package com.pombo.pombo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pombo.pombo.exception.PomboException;
import com.pombo.pombo.model.entity.Denuncia;
import com.pombo.pombo.model.repository.DenunciaRepository;

@Service
public class DenunciaService {

    @Autowired
    private DenunciaRepository denunciaRepository;

    public List<Denuncia> buscarDenunciasPorPruu(String pruuString) {
        return denunciaRepository.findByPruuUuid(pruuString);
    }

    public void atualizarSituacao(String denunciaString, Denuncia.SituacaoDenuncia situacao) throws PomboException {
        Denuncia denuncia = denunciaRepository.findById(denunciaString)
                .orElseThrow(() -> new PomboException("Denúncia não encontrada"));
        denuncia.setSituacao(situacao);
        denunciaRepository.save(denuncia);
    }

    public Denuncia criarDenuncia(Denuncia novaDenuncia) {
        return denunciaRepository.save(novaDenuncia);
    }
}
