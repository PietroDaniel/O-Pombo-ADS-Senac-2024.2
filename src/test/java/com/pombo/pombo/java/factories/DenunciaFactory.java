package com.pombo.pombo.java.factories;

import com.pombo.pombo.model.entity.Denuncia;
import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.model.enums.MotivoDenuncia;

public class DenunciaFactory {

    public static Denuncia criarDenuncia(Usuario denunciante, Pruu pruu, MotivoDenuncia motivo) {
        Denuncia denuncia = new Denuncia();

        denuncia.setDenunciante(denunciante);
        denuncia.setPruu(pruu);
        denuncia.setMotivo(motivo);

        return denuncia;
    }
}
