package com.pombo.pombo.java.factories;

import com.pombo.pombo.model.entity.Denuncia;
import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.model.enums.MotivoDenuncia;
import com.pombo.pombo.model.enums.SituacaoDenuncia;

import java.time.LocalDateTime;

public class DenunciaFactory {

    public static Denuncia criarDenuncia(Usuario denunciante, Pruu pruu, MotivoDenuncia motivo) {
        Denuncia denuncia = new Denuncia();
        denuncia.setDenunciante(denunciante);
        denuncia.setPruu(pruu);
        denuncia.setMotivo(motivo);
        denuncia.setSituacao(SituacaoDenuncia.PENDENTE);
        return denuncia;
    }

    public static Denuncia criarDenunciaCompleta(Usuario denunciante, Pruu pruu, MotivoDenuncia motivo, SituacaoDenuncia situacao, LocalDateTime dataCriacao) {
        Denuncia denuncia = new Denuncia();
        denuncia.setDenunciante(denunciante);
        denuncia.setPruu(pruu);
        denuncia.setMotivo(motivo);
        denuncia.setSituacao(situacao);
        denuncia.setDataCriacao(dataCriacao);
        return denuncia;
    }

    public static Denuncia criarDenunciaPadrao(Usuario denunciante, Pruu pruu) {
        return criarDenuncia(denunciante, pruu, MotivoDenuncia.SPAM);
    }

    public static Denuncia criarDenunciaPorMotivo(Usuario denunciante, Pruu pruu, MotivoDenuncia motivo) {
        return criarDenuncia(denunciante, pruu, motivo);
    }
}
