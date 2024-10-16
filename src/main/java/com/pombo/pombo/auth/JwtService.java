package com.pombo.pombo.auth;

import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final JwtEncoder jwtEncoder;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }
    /*
     * public String getGenerateToken(Authentication authentication) {
     * Instant now = Instant.now();
     * 
     * // será usado para definir tempo do token
     * long dezHorasEmSegundos = 36000L;
     * 
     * String rles = authentication
     * .getAuthorities().stream()
     * .map(GrantedAuthority::getAuthority)
     * .collect(Collectors
     * .joining(" "));
     * 
     * Jogador jogadorAutenticado = (Jogador) authentication.getPrincipal();
     * 
     * JwtClaimsSet claims = JwtClaimsSet.builder()
     * .issuer("vem-no-x1") // emissor do token
     * .issuedAt(now) // data/hora em que o token foi emitido
     * .expiresAt(now.plusSeconds(dezHorasEmSegundos)) // expiração do token, em
     * segundos.
     * .subject(authentication.getName()) // nome do usuário
     * .claim("roles", rles) // perfis ou permissões (roles)
     * .claim("idJogador", jogadorAutenticado.getId()) // mais propriedades
     * adicionais no token
     * .build();
     * 
     * return jwtEncoder.encode(
     * JwtEncoderParameters.from(claims))
     * .getTokenValue();
     * }
     * 
     */

}
