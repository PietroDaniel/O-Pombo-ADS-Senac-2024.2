package com.pombo.pombo.auth;

import com.pombo.pombo.exception.PomboException;
import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public AuthenticationService(JwtService jwtService, UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
    }

    public String authenticate(Authentication authentication) {
        return jwtService.generateToken(authentication);
    }

    public Usuario getAuthenticatedUser() throws PomboException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario authenticatedUser = null;

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            Jwt jwt = (Jwt) principal;
            String login = jwt.getClaim("sub");

            authenticatedUser = usuarioRepository.findByEmail(login).orElseThrow(() -> new PomboException("Usuário não encontrado."));
        }

        if(authenticatedUser == null) {
            throw new PomboException("Usuário não encontrado.");
        }

        return authenticatedUser;
    }
}
