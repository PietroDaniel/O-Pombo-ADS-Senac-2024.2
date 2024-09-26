package com.pombo.pombo.model.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "denuncias")
public class Denuncia {

   @Id 
	@UuidGenerator
	private String uuid;  

    @Column(name = "data_hora_criacao", nullable = false)
    private LocalDateTime dataHoraCriacao = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "denunciante_id", nullable = false)
    private Usuario denunciante;

    @Enumerated(EnumType.STRING)
    @Column(name = "motivo", nullable = false)
    private MotivoDenuncia motivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private SituacaoDenuncia situacao = SituacaoDenuncia.PENDENTE;

    public enum MotivoDenuncia {
        SPAM, OFENSIVO, FALSO
    }

    public enum SituacaoDenuncia {
        PENDENTE, ANALISADA
    }

    @ManyToOne
    @JoinColumn(name = "pruu_id", nullable = false)
    private Pruu pruu;

}
