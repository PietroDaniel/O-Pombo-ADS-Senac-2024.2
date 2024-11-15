package com.pombo.pombo.model.entity;

import java.time.LocalDateTime;

import com.pombo.pombo.model.dto.DenunciaDTO;
import com.pombo.pombo.model.enums.MotivoDenuncia;
import com.pombo.pombo.model.enums.SituacaoDenuncia;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import lombok.Data;

@Data
@Entity
@Table(name = "denuncias")
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "denunciante_id")
    private Usuario denunciante;

    @ManyToOne
    @JoinColumn(name = "pruu_id")
    private Pruu pruu;

    @Enumerated(EnumType.STRING)
    @Column(name = "motivo")
    private MotivoDenuncia motivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao")
    private SituacaoDenuncia situacao = SituacaoDenuncia.PENDENTE;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    public static DenunciaDTO paraDenunciaDTO(Denuncia denuncia) {
        return new DenunciaDTO(
                denuncia.getId(),
                denuncia.getDenunciante().getId(),
                denuncia.getDenunciante().getNome(),
                denuncia.getMotivo(),
                denuncia.getSituacao(),
                denuncia.getPruu().getId(),
                denuncia.getDataCriacao()
        );

    }

}
