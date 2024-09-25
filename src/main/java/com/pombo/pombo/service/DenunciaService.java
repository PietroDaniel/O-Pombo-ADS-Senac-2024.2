package com.pombo.pombo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pombo.pombo.exception.PomboException;
import com.pombo.pombo.model.entity.Denuncia;
import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.model.repository.DenunciaRepository;
import com.pombo.pombo.model.repository.PruuRepository;
import com.pombo.pombo.model.repository.UsuarioRepository;

import java.util.List;
import java.util.UUID;

@Service
public class DenunciaService {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private PruuRepository pruuRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Denuncia criarDenuncia(UUID pruuUuid, UUID usuarioUuid, Denuncia.MotivoDenuncia motivo) throws PomboException {
        Pruu pruu = pruuRepository.findByUuid(pruuUuid)
            .orElseThrow(() -> new PomboException("Pruu não encontrado"));

        Usuario denunciante = usuarioRepository.findByUuid(usuarioUuid)
            .orElseThrow(() -> new PomboException("Usuário não encontrado"));

        // Verificar se o usuário já denunciou o pruu
        if (denunciaRepository.findByPruuUuid(pruuUuid).stream()
            .anyMatch(d -> d.getDenunciante().getUuid().equals(usuarioUuid))) {
            throw new PomboException("O usuário já denunciou este pruu.");
        }

        Denuncia denuncia = new Denuncia();
        denuncia.setPruu(pruu);
        denuncia.setDenunciante(denunciante);
        denuncia.setMotivo(motivo);

        return denunciaRepository.save(denuncia);
    }

    public List<Denuncia> buscarDenunciasPorPruu(UUID pruuUuid) {
        return denunciaRepository.findByPruuUuid(pruuUuid);
    }

    public void atualizarSituacao(UUID denunciaUuid, Denuncia.SituacaoDenuncia situacao) throws PomboException {
        Denuncia denuncia = denunciaRepository.findById(denunciaUuid)
            .orElseThrow(() -> new PomboException("Denúncia não encontrada"));
        denuncia.setSituacao(situacao);
        denunciaRepository.save(denuncia);
    }
}
