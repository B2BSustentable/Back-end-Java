package com.example.b2b.dtos.usuario;

import com.example.b2b.entity.usuario.TipoUsuario;

public record UsuarioDTO(String nome, String cnpj, String senha, TipoUsuario tipoUsuario, String tipoAssinatura, int limiteDeProdutos, double desconto, boolean suporte24h, String acessoVIP) {
}
