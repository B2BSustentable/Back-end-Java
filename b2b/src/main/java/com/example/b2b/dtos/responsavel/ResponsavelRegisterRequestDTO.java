package com.example.b2b.dtos.responsavel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResponsavelRegisterRequestDTO(
        @NotBlank String nomeResponsavel,
        @NotBlank String sobrenomeResponsavel,
        @Email @NotBlank String emailResponsavel,
        @NotBlank String senhaResponsavel,
        String photoResponsavel
) {
}
