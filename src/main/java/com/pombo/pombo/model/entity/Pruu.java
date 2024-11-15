package com.pombo.pombo.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pombo.pombo.model.dto.PruuDTO;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "pruus")

public class Pruu {
    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToMany
    @JoinTable(name = "pruu_likes", joinColumns = @JoinColumn(name = "pruu_id"), inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private List<Usuario> likedByUsers = new ArrayList<>();

    @OneToMany(mappedBy = "pruu")
    @JsonBackReference
    private List<Denuncia> denuncias = new ArrayList<>();

    @NotBlank(message = "O texto do pruu é obrigatório")
    @Size(min = 1, max = 350, message = "O texto deve ter entre 1 e 300 caracteres")
    private String texto;

    @Column(name = "imagem")
    private String imagem;

    private boolean bloqueado = false;
    private boolean excluido = false;

    @Column(name = "data_hora_criacao", nullable = false, updatable = false)
    private LocalDateTime dataHoraCriacao = LocalDateTime.now();

    public static PruuDTO paraDTO(Pruu pruu, Integer quantidadeLikes, Integer quantidadeDenuncias, String imagemPruu, String fotoPerfil) {
        return new PruuDTO(
                pruu.getId(),
                pruu.getTexto(),
                imagemPruu,
                pruu.getUsuario().getId(),
                pruu.getUsuario().getNome(),
                fotoPerfil,
                quantidadeLikes,
                quantidadeDenuncias,
                pruu.getDataHoraCriacao()
        );
    }
}
