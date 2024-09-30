package com.pombo.pombo.model.dto;

import lombok.Data;

@Data
public class PruuDTO {
    private String texto;
    private int quantidadeLikes;
    private String nomeUsuario;
    private String uuidUsuario;
    private int quantidadeDenuncias;
}