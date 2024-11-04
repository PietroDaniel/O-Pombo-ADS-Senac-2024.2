package com.pombo.pombo.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany; // Adicione essa importação
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "pruus")
public class Pruu {

    @Id 
	@UuidGenerator
	private String uuid; 

    @NotBlank(message = "O texto do pruu é obrigatório")
    @Size(min = 1, max = 300, message = "O texto deve ter entre 1 e 300 caracteres")
    @Column(name = "texto", nullable = false)
    private String texto;

    @Column(name = "data_hora_criacao", nullable = false, updatable = false)
    private LocalDateTime dataHoraCriacao = LocalDateTime.now();

    @Column(name = "bloqueado", nullable = false)
    private boolean bloqueado = false;

    @Column(name = "imagem")
    private String imagem;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToMany
    @JoinTable(name = "pruu_likes", joinColumns = @JoinColumn(name = "pruu_id"), inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private List<Usuario> likedByUsers = new ArrayList<>();

    @OneToMany(mappedBy = "pruu")
    private List<Denuncia> denuncias = new ArrayList<>(); 
}
