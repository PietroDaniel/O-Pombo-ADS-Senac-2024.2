package com.pombo.pombo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pombo.pombo.exception.PomboException;
import com.pombo.pombo.model.dto.DenunciaDTO;
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

    public Denuncia criarDenuncia(Denuncia novaDenuncia) throws PomboException {
        boolean jaDenunciado = denunciaRepository.existsByDenuncianteAndPruu(novaDenuncia.getDenunciante(),
                novaDenuncia.getPruu());
        if (jaDenunciado) {
            throw new PomboException("Usuário já denunciou este pruu.");
        }
        return denunciaRepository.save(novaDenuncia);
    }

    public List<DenunciaDTO> gerarRelatorioDenuncias(String pruuUuid) {
        List<Denuncia> denuncias = denunciaRepository.findByPruuUuid(pruuUuid);
        int pendentes = (int) denuncias.stream().filter(d -> d.getSituacao() == Denuncia.SituacaoDenuncia.PENDENTE)
                .count();
        int analisadas = (int) denuncias.stream().filter(d -> d.getSituacao() == Denuncia.SituacaoDenuncia.ANALISADA)
                .count();

        DenunciaDTO dto = new DenunciaDTO();
        dto.setUuidPruu(pruuUuid);
        dto.setQuantidadeDenuncias(denuncias.size());
        dto.setQuantidadePendentes(pendentes);
        dto.setQuantidadeAnalisadas(analisadas);

        List<DenunciaDTO> relatorio = new ArrayList<>();
        relatorio.add(dto);
        return relatorio;
    }

}
