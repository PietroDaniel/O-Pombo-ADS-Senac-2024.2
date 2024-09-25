package com.pombo.pombo.model.dto;

import org.hibernate.validator.constraints.UUID;

import lombok.Data;

@Data
public class PruuDTO {
    private String texto;
    private int quantidadeLikes;
    private String nomeUsuario;
    private UUID uuidUsuario;
    private int quantidadeDenuncias;
}