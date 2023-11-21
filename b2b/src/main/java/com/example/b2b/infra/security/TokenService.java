package com.example.b2b.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.b2b.entity.empresa.Empresa;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String chaveSecreta;

    public String gerarToken(Empresa empresa) {
        try {
            Algorithm algoritmoCriptografia = Algorithm.HMAC256(chaveSecreta);
            String token = JWT.create()
                    .withIssuer("api-b2b")
                    .withSubject(empresa.getEmail())
                    .withExpiresAt(geradorDataExpiracao())
                    .sign(algoritmoCriptografia);

            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    public String validarToken(String token) {
        try {
            Algorithm algoritmoCriptografia = Algorithm.HMAC256(chaveSecreta);
            return JWT.require(algoritmoCriptografia)
                    .withIssuer("api-b2b")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao validar token", exception);
        }
    }

    public Instant geradorDataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
