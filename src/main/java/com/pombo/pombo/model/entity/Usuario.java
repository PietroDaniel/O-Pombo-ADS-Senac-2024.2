package com.pombo.pombo.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(name = "nome", nullable = false)
    private String nome;

    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @CPF(message = "CPF deve ser válido")
    @NotBlank(message = "CPF é obrigatório")
    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "foto")
    private String foto;

    @ManyToMany
    @JoinTable(name = "likes", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "pruu_id"))
    private List<Pruu> likedPruus = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    private List<Pruu> pruus = new ArrayList<>(); 
}
