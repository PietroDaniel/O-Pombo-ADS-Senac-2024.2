package com.pombo.pombo.auth;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JwtService jwtService;

    public AuthenticationService(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    /*
     * public String authenticate(Authentication authentication) {
     * return jwtService.getGenerateToken(authentication);
     * }
     * 
     */
    /*
     * public Jogador getUsuarioAutenticado() throws VemNoX1Exception {
     * Authentication authentication =
     * SecurityContextHolder.getContext().getAuthentication();
     * Jogador jogadorAutenticado = null;
     * 
     * if (authentication != null && authentication.isAuthenticated()) {
     * Object principal = authentication.getPrincipal();
     * 
     * if (principal instanceof Jogador) {
     * UserDetails userDetails = (Jogador) principal;
     * jogadorAutenticado = (Jogador) userDetails;
     * }
     * }
     * 
     * if(jogadorAutenticado == null) {
     * throw new VemNoX1Exception("Usuário não encontrado");
     * }
     * 
     * return jogadorAutenticado;
     * }
     * 
     */
}
