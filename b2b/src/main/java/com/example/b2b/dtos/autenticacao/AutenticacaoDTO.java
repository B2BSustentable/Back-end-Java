package com.example.b2b.dtos.autenticacao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AutenticacaoDTO(@Email @NotBlank String email, @NotBlank String senha) {
}
