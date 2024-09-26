package com.pombo.pombo.model.dto;

import lombok.Data;

@Data
public class DenunciaDTO {
    private String uuidPruu;
    private int quantidadeDenuncias;
    private int quantidadePendentes;
    private int quantidadeAnalisadas;
}
