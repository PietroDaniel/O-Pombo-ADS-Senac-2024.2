package com.pombo.pombo.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "denuncias")
public class Denuncia {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;  // Corrigir para UUID

    @Column(name = "data_hora_criacao", nullable = false)
    private LocalDateTime dataHoraCriacao = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "denunciante_id", nullable = false)
    private Usuario denunciante;

    @ManyToOne
    @JoinColumn(name = "pruu_id", nullable = false)
    private Pruu pruu;

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
}
