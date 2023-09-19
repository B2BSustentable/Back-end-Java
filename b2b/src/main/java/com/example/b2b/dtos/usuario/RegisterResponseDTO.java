package com.example.b2b.dtos.usuario;

public record RegisterResponseDTO(String nome, String cnpj, String senha, String email, String tipoUsuario) {
}
