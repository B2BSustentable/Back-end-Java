package com.example.b2b.dtos.usuario;

import com.example.b2b.entity.usuario.TipoUsuario;

import java.time.LocalDate;

public record RegisterDTO(String nome, String cnpj, String senha, LocalDate dataCriacao, String email, TipoUsuario tipoUsuario, String tipoAssinatura, int limiteDeProdutos, double desconto, boolean suporte24h, String acessoVIP) {
    }