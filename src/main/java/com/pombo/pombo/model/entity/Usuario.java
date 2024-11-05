package com.pombo.pombo.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pombo.pombo.model.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255, message ="O nome deve conter no máximo 255 caracteres")
    private String nome;

    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    @Column(unique = true)
    private String email;

    @CPF(message = "CPF deve ser válido")
    @NotBlank(message = "CPF é obrigatório")
    @Column(unique = true)
    private String cpf;

    @Column(columnDefinition = "TEXT")
	private String foto;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 3)
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @OneToMany(mappedBy = "usuario")
    @JsonBackReference(value = "user-publications")
    private List<Pruu> pruus = new ArrayList<>();

    @OneToMany(mappedBy = "denunciante")
    @JsonBackReference(value = "denuncias-usuario")
    private List<Denuncia> denuncia = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime criadoEm;
   
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        authorities.add(new SimpleGrantedAuthority(role.toString()));

        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

}
