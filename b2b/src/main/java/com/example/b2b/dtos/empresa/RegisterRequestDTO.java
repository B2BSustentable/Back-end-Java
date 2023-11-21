package com.example.b2b.dtos.empresa;

import com.example.b2b.entity.empresa.TipoPlanos;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.LocalDateTime;

public record RegisterRequestDTO(@NotBlank String nomeEmpresa, @CNPJ String cnpj, @NotBlank @Email String email, @NotBlank String senha, String descricao, @NotNull LocalDateTime dataDeCriacao, String photo, @NotNull TipoPlanos tipoPlanos, int limiteDeProdutos, double desconto, boolean suporte24h, String acessoVIP) {
    }