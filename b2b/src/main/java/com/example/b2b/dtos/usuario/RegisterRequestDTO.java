package com.example.b2b.dtos.usuario;

import com.example.b2b.entity.usuario.TipoUsuario;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RegisterRequestDTO(String nome, String cnpj, String senha, LocalDateTime dataDeCriacao, String email, TipoUsuario tipoUsuario, String tipoAssinatura, int limiteDeProdutos, double desconto, boolean suporte24h, String acessoVIP) {
    }