package com.pombo.pombo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pombo.pombo.auth.AuthenticationService;
import com.pombo.pombo.exception.PomboException;
import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.service.UsuarioService;

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UsuarioService usuarioService;

  /**
   * Método de login padronizado -> Basic Auth
   * 
   * O parâmetro Authentication já encapsula login (username) e senha (password)
   * Basic <Base64 encoded username and password>
   * 
   * @param authentication
   * @return o JWT gerado
   */

  @PostMapping("authenticate")
  public String authenticate(Authentication authentication) {
    return authenticationService.authenticate(authentication);
  }

  @PostMapping("/register")
  public ResponseEntity<Usuario> criarUsuario(@Valid @RequestBody Usuario novoUsuario) throws PomboException {

    String senhaCifrada = passwordEncoder.encode(novoUsuario.getPassword());
    novoUsuario.setPassword(senhaCifrada);

    Usuario usuarioCriado = usuarioService.criarUsuario(novoUsuario);

    return ResponseEntity.status(201).body(usuarioCriado);
  }

}
