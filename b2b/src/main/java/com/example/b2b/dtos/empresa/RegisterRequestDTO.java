package com.example.b2b.dtos.empresa;

import com.example.b2b.entity.empresa.TipoPlanos;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.LocalDateTime;

public record RegisterRequestDTO(@NotBlank String nome, @CNPJ String cnpj, @NotBlank String senha, LocalDateTime dataDeCriacao, @NotBlank @Email String email, @NotNull TipoPlanos tipoPlanos, @NotNull String tipoAssinatura, int limiteDeProdutos, double desconto, boolean suporte24h, String acessoVIP) {
    }