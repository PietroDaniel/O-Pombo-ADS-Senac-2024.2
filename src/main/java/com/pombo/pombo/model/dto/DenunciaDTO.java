package com.pombo.pombo.model.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class DenunciaDTO {
    private UUID uuidPruu;
    private int quantidadeDenuncias;
    private int quantidadePendentes;
    private int quantidadeAnalisadas;
}
