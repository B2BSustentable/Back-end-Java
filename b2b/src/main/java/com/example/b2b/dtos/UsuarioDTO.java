package com.example.b2b.dtos;

import com.example.b2b.entity.TipoUsuario;

public record UsuarioDTO(String nome, String cnpj, String senha, TipoUsuario tipoUsuario, String tipoAssinatura, int limiteDeProdutos, double desconto, boolean suporte24h, String acessoVIP) {
}
