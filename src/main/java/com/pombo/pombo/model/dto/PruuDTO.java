package com.pombo.pombo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PruuDTO {
    private String pruuId;
    private String pruuConteudo;
    private String pruuImagem;
    private Long usuarioId;
    private String usuarioNome;
    private String usuarioFotoPerfil;
    private Integer quantidadeLikes;
    private Integer quantidadeDenuncias;
    private LocalDateTime criadoEm;
    private Boolean excluido;
    private Boolean bloqueado;
  	private Boolean estaCurtido;
}