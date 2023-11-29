package com.example.b2b.dtos.responsavel;

public record ResponsavelRegisterResponseDTO(
        String nomeResponsavel,
        String sobrenomeResponsavel,
        String emailResponsavel,
        boolean isLogado,
        String photo
) {
}
