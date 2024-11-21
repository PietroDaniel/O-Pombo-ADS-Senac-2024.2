package com.pombo.pombo.auth;

import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.model.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(Authentication subject) {

        Instant now = Instant.now();

        long tenHoursInSeconds = 36000L;

        String roles = subject
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Object principal = subject.getPrincipal();
        Usuario authenticatedUser;
        
        if(principal instanceof Jwt) {
        	Jwt jwt = (Jwt) principal;
        	String login = jwt.getSubject();
        	
        	authenticatedUser = usuarioRepository.findByEmail(login).get();
        } else {
        	authenticatedUser = (Usuario) principal;
        }
        
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("pombo")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(tenHoursInSeconds))
                .subject(subject.getName())
                .claim("roles", roles)
                .claim("userId", authenticatedUser.getId())
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
