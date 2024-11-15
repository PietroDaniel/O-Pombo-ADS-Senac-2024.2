package com.pombo.pombo.model.dto;

import com.pombo.pombo.model.enums.MotivoDenuncia;
import com.pombo.pombo.model.enums.SituacaoDenuncia;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DenunciaDTO {
    private String denunciaId;
    private long denuncianteId;
    private String denuncianteNome;
    private MotivoDenuncia denunciaMotivo;
    private SituacaoDenuncia denunciaSituacao;
    private String pruuId;
    private LocalDateTime dataCriacao;
}
