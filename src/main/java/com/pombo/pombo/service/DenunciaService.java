package com.pombo.pombo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.enums.SituacaoDenuncia;
import com.pombo.pombo.model.repository.PruuRepository;
import com.pombo.pombo.model.seletor.DenunciaSeletor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.pombo.pombo.exception.PomboException;
import com.pombo.pombo.model.dto.DenunciaDTO;
import com.pombo.pombo.model.entity.Denuncia;
import com.pombo.pombo.model.repository.DenunciaRepository;

@Service
public class DenunciaService {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private PruuRepository pruuRepository;

    public Denuncia criarDenuncia(Denuncia novaDenuncia) throws PomboException {
        return denunciaRepository.save(novaDenuncia);
    }

    public Denuncia procurarPorId(String denunciaId) throws PomboException {
        return denunciaRepository.findById(denunciaId).orElseThrow(() -> new PomboException("Esta denúncia não foi encontrada!"));
    }

    public List<DenunciaDTO> buscarDenunciasPorPruu(String pruuId) {

        List<Denuncia> denuncias = denunciaRepository.findByPruuId(pruuId);

        return denuncias.stream().map(Denuncia::paraDenunciaDTO).collect(Collectors.toList());
    }

    public void atualizarSituacao(String denunciaId, SituacaoDenuncia novaSituacao) throws PomboException {

        Denuncia denuncia = denunciaRepository.findById(denunciaId)
                .orElseThrow(() -> new PomboException("Denúncia não encontrada"));

        Pruu pruu = denuncia.getPruu();

        if (novaSituacao == SituacaoDenuncia.ACEITA) {
            pruu.setBloqueado(true);

        } else if (denuncia.getSituacao() == SituacaoDenuncia.ACEITA &&
                (novaSituacao == SituacaoDenuncia.RECUSADA || novaSituacao == SituacaoDenuncia.PENDENTE)) {
            pruu.setBloqueado(false);
        }

        denuncia.setSituacao(novaSituacao);

        pruuRepository.save(pruu);
        denunciaRepository.save(denuncia);
    }

    public List<DenunciaDTO> listarComFiltro(DenunciaSeletor seletor) {

        List<Denuncia> denuncias = new ArrayList<>();

        if (seletor.temPaginacao()) {
            int pageNumber = seletor.getPagina();
            int pageSize = seletor.getLimite();

            PageRequest page = PageRequest.of(pageNumber - 1, pageSize);
            denuncias = denunciaRepository.findAll(seletor, page).toList();
        }

        denuncias = removerDenunciasDeletadasBloqueadas(denuncias);

        return converterParaDenunciaDTO(denuncias);
    }

    public void excluirDenuncia(String denunciaId, Long usuarioId) throws PomboException {

        Denuncia denuncia = denunciaRepository.findById(denunciaId)
                .orElseThrow(() -> new PomboException("Denúncia não encontrada"));

        if (denuncia.getDenunciante().getId().equals(usuarioId)) {
            denunciaRepository.deleteById(denunciaId);
        } else {
            throw new PomboException("Você não pode excluir denúncias de outras pessoas!");
        }
    }

    public List<DenunciaDTO> converterParaDenunciaDTO(List<Denuncia> denuncias) {

        List<DenunciaDTO> denunciasDTO = new ArrayList<>();

        for (Denuncia d : denuncias) {
            DenunciaDTO dto = Denuncia.paraDenunciaDTO(d);
            denunciasDTO.add(dto);

        }
        return denunciasDTO;
    }

    public List<Denuncia> removerDenunciasDeletadasBloqueadas(List<Denuncia> denuncias) {
        return denuncias.stream()
                .filter(denuncia -> !denuncia.getPruu().isBloqueado() && !denuncia.getPruu().isExcluido())
                .collect(Collectors.toList());
    }
}
